package com.market.analysis.model;

import com.market.analysis.model.enums.SignalStrength;
import com.market.analysis.model.enums.Trend;

import java.util.List;
import java.util.Objects;

public record TrendSignal(
        Trend trend,
        double trendScore,  // 0-100
        List<String> reasoning,
        SignalStrength signalStrength
) {
    public TrendSignal {
        Objects.requireNonNull(trend);
        Objects.requireNonNull(reasoning);
        Objects.requireNonNull(signalStrength);
    }
}