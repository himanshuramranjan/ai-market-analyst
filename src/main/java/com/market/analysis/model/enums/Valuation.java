package com.market.analysis.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Valuation {
    CHEAP(1.0, "Below fair value, potential buy"),
    FAIR(0.0, "Fair valuation"),
    EXPENSIVE(-1.0, "Above fair value, potential sell");

    private final double signalValue;
    private final String description;
}