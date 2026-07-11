package com.yaozizhenjun.uba.domain.query.model;

import com.yaozizhenjun.uba.domain.analysis.model.MetricAggregation;

public class QueryMeasure {

    private final String field;
    private final String alias;
    private final MetricAggregation aggregation;
    private final Integer rollingWindowDays;

    public QueryMeasure(String field, String alias, MetricAggregation aggregation, Integer rollingWindowDays) {
        this.field = field;
        this.alias = alias == null ? field : alias;
        this.aggregation = aggregation;
        this.rollingWindowDays = rollingWindowDays;
    }

    public String getField() {
        return field;
    }

    public String getAlias() {
        return alias;
    }

    public MetricAggregation getAggregation() {
        return aggregation;
    }

    public Integer getRollingWindowDays() {
        return rollingWindowDays;
    }

    public boolean isRolling() {
        return rollingWindowDays != null && rollingWindowDays > 1;
    }
}
