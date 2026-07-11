package com.yaozizhenjun.uba.interfaces.dto;

public class MetricRequest {

    private String field;
    private String alias;
    private String aggregation;
    private Integer rollingWindowDays;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAggregation() {
        return aggregation;
    }

    public void setAggregation(String aggregation) {
        this.aggregation = aggregation;
    }

    public Integer getRollingWindowDays() {
        return rollingWindowDays;
    }

    public void setRollingWindowDays(Integer rollingWindowDays) {
        this.rollingWindowDays = rollingWindowDays;
    }
}
