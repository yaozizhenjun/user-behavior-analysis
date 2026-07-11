package com.yaozizhenjun.uba.domain.acceleration.rule;

import com.yaozizhenjun.uba.domain.acceleration.model.AccelerationCandidate;
import com.yaozizhenjun.uba.domain.acceleration.model.AccelerationContext;
import com.yaozizhenjun.uba.domain.acceleration.model.AccelerationType;
import com.yaozizhenjun.uba.domain.acceleration.model.CostEstimate;
import com.yaozizhenjun.uba.domain.query.model.LogicalQueryPlan;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class PartitionPruningRule implements AccelerationRule {

    @Override
    public int order() {
        return 10;
    }

    @Override
    public boolean supports(LogicalQueryPlan plan, AccelerationContext context) {
        return plan.getTimeWindow() != null;
    }

    @Override
    public AccelerationCandidate apply(LogicalQueryPlan plan, AccelerationContext context) {
        Map<String, String> hints = new LinkedHashMap<String, String>();
        hints.put("partitionStart", plan.getTimeWindow().getScanStartDate().toString());
        hints.put("partitionEnd", plan.getTimeWindow().getEndDate().toString());
        return new AccelerationCandidate(
                AccelerationType.PARTITION_PRUNING,
                true,
                "time range is converted to partition boundary",
                new CostEstimate(0L, order()),
                hints
        );
    }
}
