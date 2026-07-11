package com.yaozizhenjun.uba.domain.query.model;

public class QueryDimension {

    private final String field;
    private final String alias;

    public QueryDimension(String field, String alias) {
        this.field = field;
        this.alias = alias == null ? field : alias;
    }

    public String getField() {
        return field;
    }

    public String getAlias() {
        return alias;
    }
}
