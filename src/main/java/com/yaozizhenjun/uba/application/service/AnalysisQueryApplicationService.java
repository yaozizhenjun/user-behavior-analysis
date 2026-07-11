package com.yaozizhenjun.uba.application.service;

import com.yaozizhenjun.uba.application.port.QueryEngineExecutor;
import com.yaozizhenjun.uba.application.port.QueryResultSet;
import com.yaozizhenjun.uba.application.port.SqlRenderer;
import com.yaozizhenjun.uba.domain.acceleration.model.AccelerationCandidate;
import com.yaozizhenjun.uba.domain.acceleration.model.AccelerationContext;
import com.yaozizhenjun.uba.domain.acceleration.model.AccelerationPlan;
import com.yaozizhenjun.uba.domain.acceleration.model.EngineType;
import com.yaozizhenjun.uba.domain.acceleration.service.AccelerationPlanner;
import com.yaozizhenjun.uba.domain.analysis.model.AnalysisConfig;
import com.yaozizhenjun.uba.domain.query.model.LogicalQueryPlan;
import com.yaozizhenjun.uba.domain.query.model.SqlPlan;
import com.yaozizhenjun.uba.domain.query.service.LogicalQueryPlanBuilder;
import com.yaozizhenjun.uba.infrastructure.cache.QueryCacheKeyBuilder;
import com.yaozizhenjun.uba.shared.exception.UnsupportedEngineException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnalysisQueryApplicationService {

    private final LogicalQueryPlanBuilder logicalQueryPlanBuilder;
    private final AccelerationPlanner accelerationPlanner;
    private final QueryCacheKeyBuilder queryCacheKeyBuilder;
    private final List<SqlRenderer> sqlRenderers;
    private final List<QueryEngineExecutor> executors;

    public AnalysisQueryApplicationService(LogicalQueryPlanBuilder logicalQueryPlanBuilder,
                                           AccelerationPlanner accelerationPlanner,
                                           QueryCacheKeyBuilder queryCacheKeyBuilder,
                                           List<SqlRenderer> sqlRenderers,
                                           List<QueryEngineExecutor> executors) {
        this.logicalQueryPlanBuilder = logicalQueryPlanBuilder;
        this.accelerationPlanner = accelerationPlanner;
        this.queryCacheKeyBuilder = queryCacheKeyBuilder;
        this.sqlRenderers = sqlRenderers;
        this.executors = executors;
    }

    public AnalysisQueryResult query(AnalysisConfig config, AccelerationContext context) {
        LogicalQueryPlan logicalPlan = logicalQueryPlanBuilder.build(config);
        AccelerationPlan accelerationPlan = accelerationPlanner.plan(logicalPlan, context);
        SqlPlan sqlPlan = selectRenderer(context.getEngineType()).render(
                accelerationPlan.getOptimizedPlan(),
                accelerationPlan
        );
        QueryResultSet resultSet = selectExecutor(context.getEngineType()).execute(sqlPlan);
        String cacheKey = queryCacheKeyBuilder.build(logicalPlan, accelerationPlan, context);
        return new AnalysisQueryResult(sqlPlan.getSql(), cacheKey, selectedStrategyNames(accelerationPlan), resultSet.getRows());
    }

    private SqlRenderer selectRenderer(EngineType engineType) {
        for (SqlRenderer renderer : sqlRenderers) {
            if (renderer.supports(engineType)) {
                return renderer;
            }
        }
        throw new UnsupportedEngineException("No SQL renderer found for engine " + engineType);
    }

    private QueryEngineExecutor selectExecutor(EngineType engineType) {
        for (QueryEngineExecutor executor : executors) {
            if (executor.supports(engineType)) {
                return executor;
            }
        }
        throw new UnsupportedEngineException("No query executor found for engine " + engineType);
    }

    private List<String> selectedStrategyNames(AccelerationPlan accelerationPlan) {
        List<String> names = new ArrayList<String>();
        for (AccelerationCandidate candidate : accelerationPlan.getCandidates()) {
            if (candidate.isSelected()) {
                names.add(candidate.getType().name());
            }
        }
        return names;
    }
}
