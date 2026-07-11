package com.yaozizhenjun.uba.domain.acceleration.rule;

import com.yaozizhenjun.uba.domain.acceleration.model.AccelerationCandidate;
import com.yaozizhenjun.uba.domain.acceleration.model.AccelerationContext;
import com.yaozizhenjun.uba.domain.acceleration.model.AccelerationType;
import com.yaozizhenjun.uba.domain.acceleration.model.CostEstimate;
import com.yaozizhenjun.uba.domain.analysis.model.MetricAggregation;
import com.yaozizhenjun.uba.domain.query.model.LogicalQueryPlan;
import com.yaozizhenjun.uba.domain.query.model.QueryMeasure;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class RollingMetricRewriteRule implements AccelerationRule {

    @Override
    public int order() {
        return 20;
    }

    @Override
    public boolean supports(LogicalQueryPlan plan, AccelerationContext context) {
        return plan.hasRollingMeasure() && context.getEngineCapability().isRangeJoinSupported();
    }

    @Override
    public AccelerationCandidate apply(LogicalQueryPlan plan, AccelerationContext context) {
        Map<String, String> hints = new LinkedHashMap<String, String>();
        hints.put("windowDays", String.valueOf(plan.getTimeWindow().getMaxRollingWindowDays()));
        hints.put("strategy", chooseStrategy(plan, context));
        return new AccelerationCandidate(
                AccelerationType.ROLLING_METRIC_REWRITE,
                true,
                "rolling measure is rewritten to date-series range join",
                new CostEstimate(0L, order()),
                hints
        );
    }

    private String chooseStrategy(LogicalQueryPlan plan, AccelerationContext context) {
        for (QueryMeasure measure : plan.getMeasures()) {
            if (measure.isRolling() && MetricAggregation.COUNT_DISTINCT.equals(measure.getAggregation())) {
                return context.getEngineCapability().isBitmapAggregateSupported()
                        ? "DAILY_BITMAP_ROLLUP"
                        : "DISTINCT_RANGE_JOIN";
            }
            if (measure.isRolling() && MetricAggregation.COUNT.equals(measure.getAggregation())) {
                return "DAILY_COUNT_ROLLUP";
            }
        }
        return "DETAIL_RANGE_JOIN";
    }
}
