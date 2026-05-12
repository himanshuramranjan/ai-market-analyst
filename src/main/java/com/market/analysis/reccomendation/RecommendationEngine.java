package com.market.analysis.reccomendation;

import com.market.analysis.model.RecommendationResult;
import com.market.analysis.model.TechnicalSignal;
import com.market.analysis.model.enums.Recommendation;
import com.market.analysis.model.enums.Trend;
import com.market.analysis.rules.RuleEvaluation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RecommendationEngine {

    public RecommendationResult generateRecommendation(TechnicalSignal signal, double compositeScore, double confidence) {

        long bullishCount = signal.evaluations().stream().filter(RuleEvaluation::matched).filter(evaluation -> evaluation.trend() == Trend.BULLISH).count();

        long bearishCount = signal.evaluations().stream().filter(RuleEvaluation::matched).filter(evaluation -> evaluation.trend() == Trend.BEARISH).count();

        /**
         * Strong bullish setup
         */
        if(compositeScore >= 80 && confidence >= 75 && bullishCount > bearishCount + 2) {

            return new RecommendationResult(

                    Recommendation.STRONG_ACCUMULATE,

                    "Strong bullish alignment across trend and momentum indicators with high confidence.");
        }

        /**
         * Good accumulation setup
         */
        if(compositeScore >= 65 && bullishCount > bearishCount) {

            return new RecommendationResult(Recommendation.ACCUMULATE_ON_DIPS, "Technical structure remains constructive, though waiting for pullbacks may improve entry quality.");
        }

        /**
         * Mixed signals
         */
        if(confidence < 50 && bullishCount > 0 && bearishCount > 0) {

            return new RecommendationResult(Recommendation.HOLD_AND_WAIT, "Signals remain mixed with conflicting technical conditions.");
        }

        /**
         * Weak setup
         */
        if(compositeScore < 50 && bearishCount >= bullishCount) {

            return new RecommendationResult(Recommendation.AVOID_FRESH_ENTRY, "Weak technical structure with insufficient bullish confirmation.");
        }

        /**
         * High-risk setup
         */
        if(bearishCount >= bullishCount + 2 && confidence >= 70) {

            return new RecommendationResult(Recommendation.REDUCE_EXPOSURE, "Bearish conditions dominate the current setup with elevated downside risk.");
        }

        /**
         * Default fallback
         */
        return new RecommendationResult(Recommendation.HOLD_AND_WAIT, "Current setup lacks strong directional conviction.");
    }
}