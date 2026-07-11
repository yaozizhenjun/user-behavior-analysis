package com.yaozizhenjun.uba.domain.acceleration.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class AccelerationCandidate {

    private final AccelerationType type;
    private final boolean selected;
    private final String reason;
    private final CostEstimate costEstimate;
    private final Map<String, String> hints;

    public AccelerationCandidate(AccelerationType type,
                                 boolean selected,
                                 String reason,
                                 CostEstimate costEstimate,
                                 Map<String, String> hints) {
        this.type = type;
        this.selected = selected;
        this.reason = reason;
        this.costEstimate = costEstimate;
        this.hints = hints == null
                ? Collections.<String, String>emptyMap()
                : Collections.unmodifiableMap(new LinkedHashMap<String, String>(hints));
    }

    public AccelerationType getType() {
        return type;
    }

    public boolean isSelected() {
        return selected;
    }

    public String getReason() {
        return reason;
    }

    public CostEstimate getCostEstimate() {
        return costEstimate;
    }

    public Map<String, String> getHints() {
        return hints;
    }
}
