package com.market.analysis.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConfidenceLevel {
    VERY_HIGH(90.0),
    HIGH(75.0),
    MODERATE(60.0),
    LOW(45.0),
    VERY_LOW(30.0);

    private final double threshold;
}
