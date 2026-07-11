package com.yaozizhenjun.uba.domain.acceleration.rule;

import com.yaozizhenjun.uba.domain.acceleration.model.AccelerationCandidate;
import com.yaozizhenjun.uba.domain.acceleration.model.AccelerationContext;
import com.yaozizhenjun.uba.domain.query.model.LogicalQueryPlan;

public interface AccelerationRule {

    int order();

    boolean supports(LogicalQueryPlan plan, AccelerationContext context);

    AccelerationCandidate apply(LogicalQueryPlan plan, AccelerationContext context);
}
