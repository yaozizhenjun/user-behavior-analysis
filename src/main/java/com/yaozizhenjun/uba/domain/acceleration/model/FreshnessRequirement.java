package com.yaozizhenjun.uba.domain.acceleration.model;

public class FreshnessRequirement {

    private final boolean realtimeRequired;
    private final long maxDelaySeconds;

    public FreshnessRequirement(boolean realtimeRequired, long maxDelaySeconds) {
        this.realtimeRequired = realtimeRequired;
        this.maxDelaySeconds = maxDelaySeconds;
    }

    public static FreshnessRequirement realtime() {
        return new FreshnessRequirement(true, 0L);
    }

    public boolean isRealtimeRequired() {
        return realtimeRequired;
    }

    public long getMaxDelaySeconds() {
        return maxDelaySeconds;
    }
}
