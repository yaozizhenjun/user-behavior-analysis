package com.yaozizhenjun.uba.domain.query.model;

import java.time.LocalDate;

public class QueryTimeWindow {

    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalDate scanStartDate;
    private final int maxRollingWindowDays;

    public QueryTimeWindow(LocalDate startDate, LocalDate endDate, int maxRollingWindowDays) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxRollingWindowDays = maxRollingWindowDays <= 1 ? 1 : maxRollingWindowDays;
        this.scanStartDate = startDate.minusDays(this.maxRollingWindowDays - 1L);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDate getScanStartDate() {
        return scanStartDate;
    }

    public int getMaxRollingWindowDays() {
        return maxRollingWindowDays;
    }
}
