package com.yaozizhenjun.uba.domain.query.model;

import com.yaozizhenjun.uba.domain.analysis.model.ResourceRef;
import com.yaozizhenjun.uba.domain.analysis.model.TimeGranularity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogicalQueryPlan {

    private final ResourceRef resource;
    private final QueryTimeWindow timeWindow;
    private final TimeGranularity timeGranularity;
    private final List<QueryDimension> dimensions;
    private final List<QueryMeasure> measures;
    private final List<QueryFilter> filters;
    private final QuerySignature signature;

    public LogicalQueryPlan(ResourceRef resource,
                            QueryTimeWindow timeWindow,
                            TimeGranularity timeGranularity,
                            List<QueryDimension> dimensions,
                            List<QueryMeasure> measures,
                            List<QueryFilter> filters,
                            QuerySignature signature) {
        this.resource = resource;
        this.timeWindow = timeWindow;
        this.timeGranularity = timeGranularity;
        this.dimensions = dimensions == null
                ? Collections.<QueryDimension>emptyList()
                : Collections.unmodifiableList(new ArrayList<QueryDimension>(dimensions));
        this.measures = measures == null
                ? Collections.<QueryMeasure>emptyList()
                : Collections.unmodifiableList(new ArrayList<QueryMeasure>(measures));
        this.filters = filters == null
                ? Collections.<QueryFilter>emptyList()
                : Collections.unmodifiableList(new ArrayList<QueryFilter>(filters));
        this.signature = signature;
    }

    public ResourceRef getResource() {
        return resource;
    }

    public QueryTimeWindow getTimeWindow() {
        return timeWindow;
    }

    public TimeGranularity getTimeGranularity() {
        return timeGranularity;
    }

    public List<QueryDimension> getDimensions() {
        return dimensions;
    }

    public List<QueryMeasure> getMeasures() {
        return measures;
    }

    public List<QueryFilter> getFilters() {
        return filters;
    }

    public QuerySignature getSignature() {
        return signature;
    }

    public boolean hasRollingMeasure() {
        for (QueryMeasure measure : measures) {
            if (measure.isRolling()) {
                return true;
            }
        }
        return false;
    }
}
