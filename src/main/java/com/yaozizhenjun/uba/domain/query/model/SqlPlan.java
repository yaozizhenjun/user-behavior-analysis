package com.yaozizhenjun.uba.domain.query.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SqlPlan {

    private final String sql;
    private final List<Object> parameters;

    public SqlPlan(String sql, List<Object> parameters) {
        this.sql = sql;
        this.parameters = parameters == null
                ? Collections.<Object>emptyList()
                : Collections.unmodifiableList(new ArrayList<Object>(parameters));
    }

    public String getSql() {
        return sql;
    }

    public List<Object> getParameters() {
        return parameters;
    }
}
