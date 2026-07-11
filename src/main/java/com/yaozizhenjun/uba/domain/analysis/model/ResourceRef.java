package com.yaozizhenjun.uba.domain.analysis.model;

public class ResourceRef {

    private final String resourceId;
    private final String resourceType;
    private final String physicalTable;

    public ResourceRef(String resourceId, String resourceType, String physicalTable) {
        this.resourceId = resourceId;
        this.resourceType = resourceType;
        this.physicalTable = physicalTable;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getPhysicalTable() {
        return physicalTable;
    }
}
