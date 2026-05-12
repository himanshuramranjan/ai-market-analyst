package com.market.analysis.scoring;

import com.market.analysis.model.*;
import com.market.analysis.model.enums.ConfidenceLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StandardScoringEngine implements ScoringEngine {

    @Value("${market-analysis.scoring.trend-weight:0.40}")
    private double trendWeight;

    @Value("${market-analysis.scoring.momentum-weight:0.30}")
    private double momentumWeight;

    @Value("${market-analysis.scoring.valuation-weight:0.30}")
    private double valuationWeight;

    @Override
    public ScoreCard calculate(TrendSignal trend, MomentumSignal momentum, ValuationSignal valuation) {

        log.debug("Calculating composite score");

        // Normalize scores to 0-100 if needed
        double trendScore = Math.min(100, Math.max(0, trend.trendScore()));
        double momentumScore = Math.min(100, Math.max(0, momentum.momentumScore()));
        double valuationScore = Math.min(100, Math.max(0, valuation.valuationScore()));

        // Calculate weighted composite
        double compositeScore = (trendScore * trendWeight) + (momentumScore * momentumWeight) + (valuationScore * valuationWeight);

        // Calculate confidence
        double confidence = ConfidenceCalculator.calculateConfidence(trend, momentum, valuation);

        // Determine confidence level
        ConfidenceLevel confidenceLevel = getConfidenceLevel(confidence);

        // Build explanation map
        Map<String, String> explanation = buildExplanation(trend, momentum, valuation, compositeScore);

        // Build weights map
        Map<String, Double> weights = new HashMap<>();
        weights.put("trend", trendWeight);
        weights.put("momentum", momentumWeight);
        weights.put("valuation", valuationWeight);

        ScoreCard scoreCard = new ScoreCard(trendScore, momentumScore, valuationScore, compositeScore, confidence, confidenceLevel, weights, explanation);

        log.info("Composite score: {}, Confidence: {}%", String.format("%.2f", compositeScore), String.format("%.1f", confidence));

        return scoreCard;
    }

    private ConfidenceLevel getConfidenceLevel(double confidence) {
        if(confidence >= ConfidenceLevel.VERY_HIGH.getThreshold()) {
            return ConfidenceLevel.VERY_HIGH;
        } else if(confidence >= ConfidenceLevel.HIGH.getThreshold()) {
            return ConfidenceLevel.HIGH;
        } else if(confidence >= ConfidenceLevel.MODERATE.getThreshold()) {
            return ConfidenceLevel.MODERATE;
        } else if(confidence >= ConfidenceLevel.LOW.getThreshold()) {
            return ConfidenceLevel.LOW;
        } else {
            return ConfidenceLevel.VERY_LOW;
        }
    }

    private Map<String, String> buildExplanation(TrendSignal trend, MomentumSignal momentum, ValuationSignal valuation, double compositeScore) {
        Map<String, String> explanation = new HashMap<>();

        explanation.put("trend", trend.trend() + " (" + trend.trend().getDescription() + ")");
        explanation.put("momentum", momentum.momentum() + " (" + momentum.momentum().getDescription() + ")");
        explanation.put("valuation", valuation.valuation() + " (" + valuation.valuation().getDescription() + ")");
        explanation.put("composite", "Score of " + String.format("%.2f", compositeScore) + " out of 100");
        explanation.put("interpretation", compositeScore > 70 ? "Strong buy signal" : compositeScore > 55 ? "Moderate buy signal" : compositeScore > 45 ? "Neutral signal" : compositeScore > 30 ? "Moderate sell signal" : "Strong sell signal");

        return explanation;
    }
}