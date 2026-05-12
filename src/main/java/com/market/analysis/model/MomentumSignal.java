package com.market.analysis.model;

import com.market.analysis.model.enums.Momentum;
import com.market.analysis.model.enums.SignalStrength;

import java.util.List;
import java.util.Objects;

public record MomentumSignal(Momentum momentum, double momentumScore,  // between 0-100
                             List<String> reasoning, SignalStrength signalStrength, double rsiValue
                             // Actual RSI for context
) {
    public MomentumSignal {
        Objects.requireNonNull(momentum);
        Objects.requireNonNull(reasoning);
        Objects.requireNonNull(signalStrength);
    }
}