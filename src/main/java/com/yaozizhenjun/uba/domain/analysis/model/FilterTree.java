package com.yaozizhenjun.uba.domain.analysis.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterTree {

    private final String relation;
    private final List<FilterCondition> conditions;

    public FilterTree(String relation, List<FilterCondition> conditions) {
        this.relation = relation == null ? "AND" : relation;
        this.conditions = conditions == null
                ? Collections.<FilterCondition>emptyList()
                : Collections.unmodifiableList(new ArrayList<FilterCondition>(conditions));
    }

    public static FilterTree empty() {
        return new FilterTree("AND", Collections.<FilterCondition>emptyList());
    }

    public String getRelation() {
        return relation;
    }

    public List<FilterCondition> getConditions() {
        return conditions;
    }
}
