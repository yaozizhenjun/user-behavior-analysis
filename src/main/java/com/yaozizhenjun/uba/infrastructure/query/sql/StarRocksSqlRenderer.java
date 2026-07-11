package com.yaozizhenjun.uba.infrastructure.query.sql;

import com.yaozizhenjun.uba.application.port.SqlRenderer;
import com.yaozizhenjun.uba.domain.acceleration.model.AccelerationPlan;
import com.yaozizhenjun.uba.domain.acceleration.model.EngineType;
import com.yaozizhenjun.uba.domain.analysis.model.MetricAggregation;
import com.yaozizhenjun.uba.domain.query.model.LogicalQueryPlan;
import com.yaozizhenjun.uba.domain.query.model.QueryDimension;
import com.yaozizhenjun.uba.domain.query.model.QueryFilter;
import com.yaozizhenjun.uba.domain.query.model.QueryMeasure;
import com.yaozizhenjun.uba.domain.query.model.QueryTimeWindow;
import com.yaozizhenjun.uba.domain.query.model.SqlPlan;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;

@Component
public class StarRocksSqlRenderer implements SqlRenderer {

    private static final String PARTITION_COLUMN = "dt";

    @Override
    public boolean supports(EngineType engineType) {
        return EngineType.STARROCKS.equals(engineType);
    }

    @Override
    public SqlPlan render(LogicalQueryPlan logicalQueryPlan, AccelerationPlan accelerationPlan) {
        String sql = logicalQueryPlan.hasRollingMeasure()
                ? renderRollingSql(logicalQueryPlan)
                : renderNormalSql(logicalQueryPlan);
        return new SqlPlan(sql, Collections.<Object>emptyList());
    }

    private String renderRollingSql(LogicalQueryPlan plan) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n");
        sql.append("  d.stat_date");
        for (QueryDimension dimension : plan.getDimensions()) {
            sql.append(",\n  e.").append(dimension.getField()).append(" AS ").append(dimension.getAlias());
        }
        for (QueryMeasure measure : plan.getMeasures()) {
            sql.append(",\n  ").append(renderMeasure(measure, "e")).append(" AS ").append(measure.getAlias());
        }
        sql.append("\nFROM (\n");
        sql.append(renderDateSeries(plan.getTimeWindow()));
        sql.append("\n) d\n");
        sql.append("LEFT JOIN (\n");
        sql.append("  SELECT *\n");
        sql.append("  FROM ").append(tableName(plan)).append("\n");
        sql.append("  WHERE ").append(PARTITION_COLUMN).append(" >= DATE '")
                .append(plan.getTimeWindow().getScanStartDate()).append("'\n");
        sql.append("    AND ").append(PARTITION_COLUMN).append(" <= DATE '")
                .append(plan.getTimeWindow().getEndDate()).append("'");
        appendFilters(sql, plan, "    ");
        sql.append("\n) e\n");
        sql.append("  ON e.").append(PARTITION_COLUMN).append(" >= d.window_start\n");
        sql.append(" AND e.").append(PARTITION_COLUMN).append(" <= d.stat_date\n");
        sql.append("GROUP BY\n");
        sql.append("  d.stat_date");
        for (QueryDimension dimension : plan.getDimensions()) {
            sql.append(",\n  e.").append(dimension.getField());
        }
        sql.append("\nORDER BY\n");
        sql.append("  d.stat_date");
        for (QueryDimension dimension : plan.getDimensions()) {
            sql.append(",\n  e.").append(dimension.getField());
        }
        return sql.toString();
    }

    private String renderNormalSql(LogicalQueryPlan plan) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n");
        sql.append("  ").append(PARTITION_COLUMN).append(" AS stat_date");
        for (QueryDimension dimension : plan.getDimensions()) {
            sql.append(",\n  ").append(dimension.getField()).append(" AS ").append(dimension.getAlias());
        }
        for (QueryMeasure measure : plan.getMeasures()) {
            sql.append(",\n  ").append(renderMeasure(measure, null)).append(" AS ").append(measure.getAlias());
        }
        sql.append("\nFROM ").append(tableName(plan)).append("\n");
        sql.append("WHERE ").append(PARTITION_COLUMN).append(" >= DATE '")
                .append(plan.getTimeWindow().getStartDate()).append("'\n");
        sql.append("  AND ").append(PARTITION_COLUMN).append(" <= DATE '")
                .append(plan.getTimeWindow().getEndDate()).append("'");
        appendFilters(sql, plan, "  ");
        sql.append("\nGROUP BY\n");
        sql.append("  ").append(PARTITION_COLUMN);
        for (QueryDimension dimension : plan.getDimensions()) {
            sql.append(",\n  ").append(dimension.getField());
        }
        sql.append("\nORDER BY\n");
        sql.append("  stat_date");
        for (QueryDimension dimension : plan.getDimensions()) {
            sql.append(",\n  ").append(dimension.getAlias());
        }
        return sql.toString();
    }

    private String renderDateSeries(QueryTimeWindow timeWindow) {
        StringBuilder series = new StringBuilder();
        LocalDate current = timeWindow.getStartDate();
        boolean first = true;
        while (!current.isAfter(timeWindow.getEndDate())) {
            if (!first) {
                series.append("\n  UNION ALL ");
            } else {
                series.append("  ");
            }
            LocalDate windowStart = current.minusDays(timeWindow.getMaxRollingWindowDays() - 1L);
            series.append("SELECT DATE '").append(current).append("' AS stat_date, ")
                    .append("DATE '").append(windowStart).append("' AS window_start");
            current = current.plusDays(1L);
            first = false;
        }
        return series.toString();
    }

    private String renderMeasure(QueryMeasure measure, String tableAlias) {
        String field = qualify(measure.getField(), tableAlias);
        if (MetricAggregation.COUNT_DISTINCT.equals(measure.getAggregation())) {
            return "COUNT(DISTINCT " + field + ")";
        }
        if (MetricAggregation.COUNT.equals(measure.getAggregation())) {
            return measure.getField() == null || measure.getField().trim().length() == 0
                    ? "COUNT(*)"
                    : "COUNT(" + field + ")";
        }
        if (MetricAggregation.SUM.equals(measure.getAggregation())) {
            return "SUM(" + field + ")";
        }
        if (MetricAggregation.AVG.equals(measure.getAggregation())) {
            return "AVG(" + field + ")";
        }
        if (MetricAggregation.MAX.equals(measure.getAggregation())) {
            return "MAX(" + field + ")";
        }
        if (MetricAggregation.MIN.equals(measure.getAggregation())) {
            return "MIN(" + field + ")";
        }
        throw new IllegalArgumentException("Unsupported aggregation " + measure.getAggregation());
    }

    private void appendFilters(StringBuilder sql, LogicalQueryPlan plan, String indent) {
        for (QueryFilter filter : plan.getFilters()) {
            sql.append("\n").append(indent).append("AND ").append(renderFilter(filter));
        }
    }

    private String renderFilter(QueryFilter filter) {
        String operator = filter.getOperator() == null ? "EQ" : filter.getOperator().toUpperCase();
        if ("EQ".equals(operator)) {
            return filter.getField() + " = '" + escape(filter.getValue()) + "'";
        }
        if ("NE".equals(operator)) {
            return filter.getField() + " <> '" + escape(filter.getValue()) + "'";
        }
        if ("GT".equals(operator)) {
            return filter.getField() + " > '" + escape(filter.getValue()) + "'";
        }
        if ("GTE".equals(operator)) {
            return filter.getField() + " >= '" + escape(filter.getValue()) + "'";
        }
        if ("LT".equals(operator)) {
            return filter.getField() + " < '" + escape(filter.getValue()) + "'";
        }
        if ("LTE".equals(operator)) {
            return filter.getField() + " <= '" + escape(filter.getValue()) + "'";
        }
        return filter.getField() + " = '" + escape(filter.getValue()) + "'";
    }

    private String qualify(String field, String tableAlias) {
        if (tableAlias == null || tableAlias.trim().length() == 0) {
            return field;
        }
        return tableAlias + "." + field;
    }

    private String tableName(LogicalQueryPlan plan) {
        if (plan.getResource().getPhysicalTable() == null || plan.getResource().getPhysicalTable().trim().length() == 0) {
            return "event_table";
        }
        return plan.getResource().getPhysicalTable();
    }

    private String escape(String value) {
        return value == null ? "" : value.replace("'", "''");
    }
}
