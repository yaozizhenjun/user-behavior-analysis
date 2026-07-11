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
public class MaterializedViewMatchRule implements AccelerationRule {

    @Override
    public int order() {
        return 30;
    }

    @Override
    public boolean supports(LogicalQueryPlan plan, AccelerationContext context) {
        return context.getEngineCapability().isMaterializedViewSupported() && !context.isPreRun();
    }

    @Override
    public AccelerationCandidate apply(LogicalQueryPlan plan, AccelerationContext context) {
        Map<String, String> hints = new LinkedHashMap<String, String>();
        hints.put("matchKey", plan.getSignature().getValue());
        return new AccelerationCandidate(
                AccelerationType.MATERIALIZED_VIEW_MATCH,
                false,
                "materialized view metadata repository is not wired yet",
                new CostEstimate(0L, order()),
                hints
        );
    }
}
