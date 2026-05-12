package com.market.analysis.scoring;

import com.market.analysis.model.*;

public interface ScoringEngine {

    /**
     * Calculate scores from signals
     * @param trend trend signal
     * @param momentum momentum signal
     * @param valuation valuation signal
     * @return scorecard with all scores
     */
    ScoreCard calculate(TrendSignal trend,
                        MomentumSignal momentum,
                        ValuationSignal valuation);
}