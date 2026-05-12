package com.market.analysis.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Momentum {
    STRONG(1.0, "Strong positive momentum"),
    WEAK(-1.0, "Weak or negative momentum"),
    NEUTRAL(0.0, "Balanced momentum state");

    private final double signalValue;
    private final String description;
}