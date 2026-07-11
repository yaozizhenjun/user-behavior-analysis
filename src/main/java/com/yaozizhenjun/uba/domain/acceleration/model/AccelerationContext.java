package com.yaozizhenjun.uba.domain.acceleration.model;

public class AccelerationContext {

    private final EngineType engineType;
    private final String tenantId;
    private final String userId;
    private final boolean preRun;
    private final EngineCapability engineCapability;
    private final FreshnessRequirement freshnessRequirement;

    public AccelerationContext(EngineType engineType,
                               String tenantId,
                               String userId,
                               boolean preRun,
                               EngineCapability engineCapability,
                               FreshnessRequirement freshnessRequirement) {
        this.engineType = engineType;
        this.tenantId = tenantId;
        this.userId = userId;
        this.preRun = preRun;
        this.engineCapability = engineCapability;
        this.freshnessRequirement = freshnessRequirement;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isPreRun() {
        return preRun;
    }

    public EngineCapability getEngineCapability() {
        return engineCapability;
    }

    public FreshnessRequirement getFreshnessRequirement() {
        return freshnessRequirement;
    }
}
