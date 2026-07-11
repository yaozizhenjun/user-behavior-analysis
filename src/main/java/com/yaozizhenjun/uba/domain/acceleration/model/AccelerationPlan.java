package com.yaozizhenjun.uba.domain.acceleration.model;

import com.yaozizhenjun.uba.domain.query.model.LogicalQueryPlan;
import com.yaozizhenjun.uba.domain.query.model.QuerySignature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccelerationPlan {

    private final LogicalQueryPlan originalPlan;
    private final LogicalQueryPlan optimizedPlan;
    private final EngineType targetEngine;
    private final List<AccelerationCandidate> candidates;
    private final QuerySignature cacheSignature;

    public AccelerationPlan(LogicalQueryPlan originalPlan,
                            LogicalQueryPlan optimizedPlan,
                            EngineType targetEngine,
                            List<AccelerationCandidate> candidates,
                            QuerySignature cacheSignature) {
        this.originalPlan = originalPlan;
        this.optimizedPlan = optimizedPlan;
        this.targetEngine = targetEngine;
        this.candidates = candidates == null
                ? Collections.<AccelerationCandidate>emptyList()
                : Collections.unmodifiableList(new ArrayList<AccelerationCandidate>(candidates));
        this.cacheSignature = cacheSignature;
    }

    public LogicalQueryPlan getOriginalPlan() {
        return originalPlan;
    }

    public LogicalQueryPlan getOptimizedPlan() {
        return optimizedPlan;
    }

    public EngineType getTargetEngine() {
        return targetEngine;
    }

    public List<AccelerationCandidate> getCandidates() {
        return candidates;
    }

    public QuerySignature getCacheSignature() {
        return cacheSignature;
    }
}
