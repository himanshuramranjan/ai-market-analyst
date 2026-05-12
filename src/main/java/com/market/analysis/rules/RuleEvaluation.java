package com.market.analysis.rules;

import com.market.analysis.model.enums.RuleCategory;
import com.market.analysis.model.enums.SignalStrength;
import com.market.analysis.model.enums.Trend;

public record RuleEvaluation(boolean matched, double score, Trend trend, SignalStrength strength, RuleCategory category,
                             double weight, String reason) {
    public static RuleEvaluation bullish(double score, SignalStrength strength, RuleCategory category, double weight, String reason) {
        return new RuleEvaluation(true, score, Trend.BULLISH, strength, category, weight, reason);
    }

    public static RuleEvaluation bearish(double score, SignalStrength strength, RuleCategory category, double weight, String reason) {
        return new RuleEvaluation(true, score, Trend.BEARISH, strength, category, weight, reason);
    }

    public static RuleEvaluation neutral(RuleCategory category, double weight, String reason) {
        return new RuleEvaluation(false, 50, Trend.NEUTRAL, SignalStrength.WEAK, category, weight, reason);
    }
}