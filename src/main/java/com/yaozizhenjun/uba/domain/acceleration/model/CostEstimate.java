package com.yaozizhenjun.uba.domain.acceleration.model;

public class CostEstimate {

    private final long estimatedRows;
    private final int priority;

    public CostEstimate(long estimatedRows, int priority) {
        this.estimatedRows = estimatedRows;
        this.priority = priority;
    }

    public long getEstimatedRows() {
        return estimatedRows;
    }

    public int getPriority() {
        return priority;
    }
}
