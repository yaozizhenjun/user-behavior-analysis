package com.yaozizhenjun.uba.interfaces.dto;

import java.util.List;
import java.util.Map;

public class AnalysisQueryResponse {

    private String sql;
    private String cacheKey;
    private List<String> accelerationStrategies;
    private List<Map<String, Object>> rows;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public List<String> getAccelerationStrategies() {
        return accelerationStrategies;
    }

    public void setAccelerationStrategies(List<String> accelerationStrategies) {
        this.accelerationStrategies = accelerationStrategies;
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }
}
