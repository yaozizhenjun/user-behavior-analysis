package com.yaozizhenjun.uba.application.port;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class QueryResultSet {

    private final List<Map<String, Object>> rows;

    public QueryResultSet(List<Map<String, Object>> rows) {
        this.rows = rows == null
                ? Collections.<Map<String, Object>>emptyList()
                : Collections.unmodifiableList(new ArrayList<Map<String, Object>>(rows));
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }
}
