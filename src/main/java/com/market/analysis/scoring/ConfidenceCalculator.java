package com.market.analysis.scoring;

import com.market.analysis.model.*;
import com.market.analysis.model.enums.Momentum;
import com.market.analysis.model.enums.Trend;
import com.market.analysis.model.enums.Valuation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfidenceCalculator {

    /**
     * Confidence measures:
     * - signal agreement
     * - directional consistency
     * - strength reliability
     * NOT bullishness.
     */
    public static double calculateConfidence(TrendSignal trend, MomentumSignal momentum, ValuationSignal valuation) {

        int bullishSignals = 0;
        int bearishSignals = 0;
        int neutralSignals = 0;

        /**
         * Trend
         */
        switch(trend.trend()) {

            case BULLISH -> bullishSignals++;

            case BEARISH -> bearishSignals++;

            case NEUTRAL -> neutralSignals++;
        }

        /**
         * Momentum
         */
        switch(momentum.momentum()) {

            case STRONG -> bullishSignals++;

            case WEAK -> bearishSignals++;

            case NEUTRAL -> neutralSignals++;
        }

        /**
         * Valuation
         */
        switch(valuation.valuation()) {

            case CHEAP -> bullishSignals++;

            case EXPENSIVE -> bearishSignals++;

            case FAIR -> neutralSignals++;
        }

        /**
         * Directional agreement
         */
        int dominantSignals = Math.max(bullishSignals, bearishSignals);

        double alignmentConfidence = (dominantSignals / 3.0) * 70;

        /**
         * Signal strength bonus
         */
        double averageStrength = (trend.signalStrength().getStrength() + momentum.signalStrength().getStrength() + valuation.signalStrength().getStrength()) / 3.0;

        double strengthBonus = (averageStrength / 100.0) * 30;

        double totalConfidence = Math.min(100, alignmentConfidence + strengthBonus);

        log.debug("Confidence -> bullish={}, bearish={}, neutral={}, alignment={}, strengthBonus={}, total={}", bullishSignals, bearishSignals, neutralSignals, alignmentConfidence, strengthBonus, totalConfidence);

        return totalConfidence;
    }
}