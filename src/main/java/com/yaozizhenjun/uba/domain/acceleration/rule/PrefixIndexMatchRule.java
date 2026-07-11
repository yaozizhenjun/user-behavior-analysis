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
public class PrefixIndexMatchRule implements AccelerationRule {

    @Override
    public int order() {
        return 40;
    }

    @Override
    public boolean supports(LogicalQueryPlan plan, AccelerationContext context) {
        return context.getEngineCapability().isPrefixIndexSupported()
                && (!plan.getFilters().isEmpty() || !plan.getDimensions().isEmpty());
    }

    @Override
    public AccelerationCandidate apply(LogicalQueryPlan plan, AccelerationContext context) {
        Map<String, String> hints = new LinkedHashMap<String, String>();
        hints.put("filterCount", String.valueOf(plan.getFilters().size()));
        hints.put("dimensionCount", String.valueOf(plan.getDimensions().size()));
        return new AccelerationCandidate(
                AccelerationType.PREFIX_INDEX_MATCH,
                true,
                "prefix-index friendly fields are detected for SQL rendering",
                new CostEstimate(0L, order()),
                hints
        );
    }
}
