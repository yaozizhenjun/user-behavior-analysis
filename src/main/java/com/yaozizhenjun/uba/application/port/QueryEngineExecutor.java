package com.yaozizhenjun.uba.application.port;

import com.yaozizhenjun.uba.domain.acceleration.model.EngineType;
import com.yaozizhenjun.uba.domain.query.model.SqlPlan;

public interface QueryEngineExecutor {

    boolean supports(EngineType engineType);

    QueryResultSet execute(SqlPlan sqlPlan);
}
