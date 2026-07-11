package com.yaozizhenjun.uba.application.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AnalysisQueryResult {

    private final String sql;
    private final String cacheKey;
    private final List<String> accelerationStrategies;
    private final List<Map<String, Object>> rows;

    public AnalysisQueryResult(String sql,
                               String cacheKey,
                               List<String> accelerationStrategies,
                               List<Map<String, Object>> rows) {
        this.sql = sql;
        this.cacheKey = cacheKey;
        this.accelerationStrategies = accelerationStrategies == null
                ? Collections.<String>emptyList()
                : Collections.unmodifiableList(new ArrayList<String>(accelerationStrategies));
        this.rows = rows == null
                ? Collections.<Map<String, Object>>emptyList()
                : Collections.unmodifiableList(new ArrayList<Map<String, Object>>(rows));
    }

    public String getSql() {
        return sql;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public List<String> getAccelerationStrategies() {
        return accelerationStrategies;
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }
}
