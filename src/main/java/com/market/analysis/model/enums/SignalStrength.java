package com.market.analysis.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SignalStrength {
    VERY_STRONG(90, "Clear, unambiguous signal"),
    STRONG(75, "Good signal with minor uncertainty"),
    MODERATE(50, "Moderate signal"),
    WEAK(25, "Weak signal, use with caution");

    private final int strength;
    private final String description;
}