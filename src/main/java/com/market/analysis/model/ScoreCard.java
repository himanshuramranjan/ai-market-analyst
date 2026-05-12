package com.market.analysis.model;

import com.market.analysis.model.enums.ConfidenceLevel;

import java.util.Map;
import java.util.Objects;

public record ScoreCard(
        double trendScore,           // 0-100
        double momentumScore,        // 0-100
        double valuationScore,       // 0-100
        double compositeScore,       // weighted 0-100
        double confidence,           // 0-100%
        ConfidenceLevel confidenceLevel,
        Map<String, Double> componentWeights,
        Map<String, String> scoreExplanation
) {
    public ScoreCard {
        Objects.requireNonNull(confidenceLevel);
        Objects.requireNonNull(componentWeights);
        Objects.requireNonNull(scoreExplanation);
    }
}