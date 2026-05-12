package com.market.analysis.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Trend {
    BULLISH(1.0, "Strong uptrend, price above key MAs"),
    BEARISH(-1.0, "Strong downtrend, price below key MAs"),
    NEUTRAL(0.0, "No clear direction");

    private final double signalValue;
    private final String description;
}
