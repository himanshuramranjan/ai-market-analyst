package com.market.analysis.model;

import com.market.analysis.model.enums.SignalStrength;
import com.market.analysis.model.enums.Valuation;

import java.util.List;
import java.util.Objects;

public record ValuationSignal(
        Valuation valuation,
        double valuationScore,  // 0-100
        List<String> reasoning,
        SignalStrength signalStrength,
        double peValue  // Actual PE for context
) {
    public ValuationSignal {
        Objects.requireNonNull(valuation);
        Objects.requireNonNull(reasoning);
        Objects.requireNonNull(signalStrength);
    }
}