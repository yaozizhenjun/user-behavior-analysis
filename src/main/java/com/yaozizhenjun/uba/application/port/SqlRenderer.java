package com.yaozizhenjun.uba.application.port;

import com.yaozizhenjun.uba.domain.acceleration.model.AccelerationPlan;
import com.yaozizhenjun.uba.domain.acceleration.model.EngineType;
import com.yaozizhenjun.uba.domain.query.model.LogicalQueryPlan;
import com.yaozizhenjun.uba.domain.query.model.SqlPlan;

public interface SqlRenderer {

    boolean supports(EngineType engineType);

    SqlPlan render(LogicalQueryPlan logicalQueryPlan, AccelerationPlan accelerationPlan);
}
