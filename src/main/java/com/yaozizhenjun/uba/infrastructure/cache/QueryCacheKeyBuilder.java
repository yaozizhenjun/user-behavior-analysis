package com.yaozizhenjun.uba.infrastructure.cache;

import com.yaozizhenjun.uba.domain.acceleration.model.AccelerationContext;
import com.yaozizhenjun.uba.domain.acceleration.model.AccelerationPlan;
import com.yaozizhenjun.uba.domain.query.model.LogicalQueryPlan;
import org.springframework.stereotype.Component;

@Component
public class QueryCacheKeyBuilder {

    public String build(LogicalQueryPlan logicalPlan,
                        AccelerationPlan accelerationPlan,
                        AccelerationContext context) {
        return "analysis:"
                + context.getTenantId()
                + ":" + context.getUserId()
                + ":" + logicalPlan.getSignature().getValue()
                + ":" + accelerationPlan.getCacheSignature().getValue();
    }
}
