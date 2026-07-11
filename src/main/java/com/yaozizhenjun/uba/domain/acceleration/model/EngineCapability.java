package com.yaozizhenjun.uba.domain.acceleration.model;

public class EngineCapability {

    private final boolean materializedViewSupported;
    private final boolean prefixIndexSupported;
    private final boolean bitmapAggregateSupported;
    private final boolean rangeJoinSupported;

    public EngineCapability(boolean materializedViewSupported,
                            boolean prefixIndexSupported,
                            boolean bitmapAggregateSupported,
                            boolean rangeJoinSupported) {
        this.materializedViewSupported = materializedViewSupported;
        this.prefixIndexSupported = prefixIndexSupported;
        this.bitmapAggregateSupported = bitmapAggregateSupported;
        this.rangeJoinSupported = rangeJoinSupported;
    }

    public static EngineCapability starRocksDefault() {
        return new EngineCapability(true, true, true, true);
    }

    public boolean isMaterializedViewSupported() {
        return materializedViewSupported;
    }

    public boolean isPrefixIndexSupported() {
        return prefixIndexSupported;
    }

    public boolean isBitmapAggregateSupported() {
        return bitmapAggregateSupported;
    }

    public boolean isRangeJoinSupported() {
        return rangeJoinSupported;
    }
}
