package com.yaozizhenjun.uba.domain.analysis.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnalysisConfig {

    private final ResourceRef resource;
    private final TimeRange timeRange;
    private final TimeGranularity timeGranularity;
    private final List<DimensionSpec> dimensions;
    private final List<MetricSpec> metrics;
    private final FilterTree filters;

    public AnalysisConfig(ResourceRef resource,
                          TimeRange timeRange,
                          TimeGranularity timeGranularity,
                          List<DimensionSpec> dimensions,
                          List<MetricSpec> metrics,
                          FilterTree filters) {
        this.resource = resource;
        this.timeRange = timeRange;
        this.timeGranularity = timeGranularity == null ? TimeGranularity.DAY : timeGranularity;
        this.dimensions = dimensions == null
                ? Collections.<DimensionSpec>emptyList()
                : Collections.unmodifiableList(new ArrayList<DimensionSpec>(dimensions));
        this.metrics = metrics == null
                ? Collections.<MetricSpec>emptyList()
                : Collections.unmodifiableList(new ArrayList<MetricSpec>(metrics));
        this.filters = filters == null ? FilterTree.empty() : filters;
    }

    public ResourceRef getResource() {
        return resource;
    }

    public TimeRange getTimeRange() {
        return timeRange;
    }

    public TimeGranularity getTimeGranularity() {
        return timeGranularity;
    }

    public List<DimensionSpec> getDimensions() {
        return dimensions;
    }

    public List<MetricSpec> getMetrics() {
        return metrics;
    }

    public FilterTree getFilters() {
        return filters;
    }
}
