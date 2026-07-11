package com.yaozizhenjun.uba.domain.analysis.model;

public class DimensionSpec {

    private final String field;
    private final String alias;

    public DimensionSpec(String field, String alias) {
        this.field = field;
        this.alias = alias;
    }

    public String getField() {
        return field;
    }

    public String getAlias() {
        return alias;
    }
}
