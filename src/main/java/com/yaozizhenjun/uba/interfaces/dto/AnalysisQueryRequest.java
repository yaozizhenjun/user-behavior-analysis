package com.yaozizhenjun.uba.interfaces.dto;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class AnalysisQueryRequest {

    private String tenantId;
    private String userId;
    private boolean preRun;

    @NotBlank
    private String resourceId;
    private String resourceType;
    private String physicalTable;

    @NotBlank
    private String startDate;

    @NotBlank
    private String endDate;

    private String timeGranularity;
    private List<DimensionRequest> dimensions;
    private List<MetricRequest> metrics;
    private List<FilterRequest> filters;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isPreRun() {
        return preRun;
    }

    public void setPreRun(boolean preRun) {
        this.preRun = preRun;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getPhysicalTable() {
        return physicalTable;
    }

    public void setPhysicalTable(String physicalTable) {
        this.physicalTable = physicalTable;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTimeGranularity() {
        return timeGranularity;
    }

    public void setTimeGranularity(String timeGranularity) {
        this.timeGranularity = timeGranularity;
    }

    public List<DimensionRequest> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<DimensionRequest> dimensions) {
        this.dimensions = dimensions;
    }

    public List<MetricRequest> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<MetricRequest> metrics) {
        this.metrics = metrics;
    }

    public List<FilterRequest> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterRequest> filters) {
        this.filters = filters;
    }
}
