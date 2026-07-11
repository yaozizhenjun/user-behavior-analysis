package com.yaozizhenjun.uba.domain.analysis.model;

public class FilterCondition {

    private final String field;
    private final String operator;
    private final String value;

    public FilterCondition(String field, String operator, String value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public String getOperator() {
        return operator;
    }

    public String getValue() {
        return value;
    }
}
