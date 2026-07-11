package com.yaozizhenjun.uba.domain.acceleration.service;

import com.yaozizhenjun.uba.domain.acceleration.model.AccelerationCandidate;
import com.yaozizhenjun.uba.domain.acceleration.model.AccelerationContext;
import com.yaozizhenjun.uba.domain.acceleration.model.AccelerationPlan;
import com.yaozizhenjun.uba.domain.acceleration.rule.AccelerationRule;
import com.yaozizhenjun.uba.domain.query.model.LogicalQueryPlan;
import com.yaozizhenjun.uba.domain.query.model.QuerySignature;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class AccelerationPlanner {

    private final List<AccelerationRule> rules;

    public AccelerationPlanner(List<AccelerationRule> rules) {
        this.rules = new ArrayList<AccelerationRule>(rules);
        Collections.sort(this.rules, new Comparator<AccelerationRule>() {
            @Override
            public int compare(AccelerationRule left, AccelerationRule right) {
                return left.order() - right.order();
            }
        });
    }

    public AccelerationPlan plan(LogicalQueryPlan logicalQueryPlan, AccelerationContext context) {
        List<AccelerationCandidate> candidates = new ArrayList<AccelerationCandidate>();
        for (AccelerationRule rule : rules) {
            if (rule.supports(logicalQueryPlan, context)) {
                candidates.add(rule.apply(logicalQueryPlan, context));
            }
        }
        QuerySignature cacheSignature = new QuerySignature(
                logicalQueryPlan.getSignature().getValue() + "|engine=" + context.getEngineType() + "|acc=" + candidates.size()
        );
        return new AccelerationPlan(
                logicalQueryPlan,
                logicalQueryPlan,
                context.getEngineType(),
                candidates,
                cacheSignature
        );
    }
}
