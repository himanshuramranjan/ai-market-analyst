package com.market.analysis.model;

import com.market.analysis.rules.RuleEvaluation;

import java.util.List;
import java.util.Objects;

public record TechnicalSignal(
        String symbol,
        TrendSignal trendSignal,
        MomentumSignal momentumSignal,
        ValuationSignal valuationSignal,
        List<RuleEvaluation> evaluations
) {
    public TechnicalSignal {
        Objects.requireNonNull(symbol);
        Objects.requireNonNull(trendSignal);
        Objects.requireNonNull(momentumSignal);
        Objects.requireNonNull(valuationSignal);
        Objects.requireNonNull(evaluations);
    }
}