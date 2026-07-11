package com.yaozizhenjun.uba.domain.query.service;

import com.yaozizhenjun.uba.domain.analysis.model.AnalysisConfig;
import com.yaozizhenjun.uba.domain.analysis.model.DimensionSpec;
import com.yaozizhenjun.uba.domain.analysis.model.FilterCondition;
import com.yaozizhenjun.uba.domain.analysis.model.MetricSpec;
import com.yaozizhenjun.uba.domain.query.model.LogicalQueryPlan;
import com.yaozizhenjun.uba.domain.query.model.QueryDimension;
import com.yaozizhenjun.uba.domain.query.model.QueryFilter;
import com.yaozizhenjun.uba.domain.query.model.QueryMeasure;
import com.yaozizhenjun.uba.domain.query.model.QuerySignature;
import com.yaozizhenjun.uba.domain.query.model.QueryTimeWindow;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LogicalQueryPlanBuilder {

    public LogicalQueryPlan build(AnalysisConfig config) {
        List<QueryDimension> dimensions = toDimensions(config.getDimensions());
        List<QueryMeasure> measures = toMeasures(config.getMetrics());
        List<QueryFilter> filters = toFilters(config.getFilters().getConditions());
        QueryTimeWindow timeWindow = new QueryTimeWindow(
                config.getTimeRange().getStartDate(),
                config.getTimeRange().getEndDate(),
                maxRollingWindowDays(measures)
        );
        return new LogicalQueryPlan(
                config.getResource(),
                timeWindow,
                config.getTimeGranularity(),
                dimensions,
                measures,
                filters,
                buildSignature(config)
        );
    }

    private List<QueryDimension> toDimensions(List<DimensionSpec> dimensionSpecs) {
        List<QueryDimension> dimensions = new ArrayList<QueryDimension>();
        for (DimensionSpec spec : dimensionSpecs) {
            dimensions.add(new QueryDimension(spec.getField(), spec.getAlias()));
        }
        return dimensions;
    }

    private List<QueryMeasure> toMeasures(List<MetricSpec> metricSpecs) {
        List<QueryMeasure> measures = new ArrayList<QueryMeasure>();
        for (MetricSpec spec : metricSpecs) {
            measures.add(new QueryMeasure(
                    spec.getField(),
                    spec.getAlias(),
                    spec.getAggregation(),
                    spec.getRollingWindowDays()
            ));
        }
        return measures;
    }

    private List<QueryFilter> toFilters(List<FilterCondition> conditions) {
        List<QueryFilter> filters = new ArrayList<QueryFilter>();
        for (FilterCondition condition : conditions) {
            filters.add(new QueryFilter(condition.getField(), condition.getOperator(), condition.getValue()));
        }
        return filters;
    }

    private int maxRollingWindowDays(List<QueryMeasure> measures) {
        int max = 1;
        for (QueryMeasure measure : measures) {
            if (measure.getRollingWindowDays() != null && measure.getRollingWindowDays() > max) {
                max = measure.getRollingWindowDays();
            }
        }
        return max;
    }

    private QuerySignature buildSignature(AnalysisConfig config) {
        String signature = config.getResource().getResourceId()
                + "|" + config.getTimeRange().getStartDate()
                + "|" + config.getTimeRange().getEndDate()
                + "|" + config.getTimeGranularity()
                + "|d" + config.getDimensions().size()
                + "|m" + config.getMetrics().size()
                + "|f" + config.getFilters().getConditions().size();
        return new QuerySignature(signature);
    }
}
