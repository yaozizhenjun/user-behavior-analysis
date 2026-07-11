package com.yaozizhenjun.uba.infrastructure.query.executor;

import com.yaozizhenjun.uba.application.port.QueryEngineExecutor;
import com.yaozizhenjun.uba.application.port.QueryResultSet;
import com.yaozizhenjun.uba.domain.acceleration.model.EngineType;
import com.yaozizhenjun.uba.domain.query.model.SqlPlan;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class StarRocksQueryEngineExecutor implements QueryEngineExecutor {

    @Override
    public boolean supports(EngineType engineType) {
        return EngineType.STARROCKS.equals(engineType);
    }

    @Override
    public QueryResultSet execute(SqlPlan sqlPlan) {
        // The first framework version returns an empty set.
        // Wire JdbcTemplate or the existing StarRocks client here in the integration phase.
        return new QueryResultSet(Collections.<java.util.Map<String, Object>>emptyList());
    }
}
