package com.market.analysis.rules;

import com.market.analysis.model.StockMetrics;
import com.market.analysis.model.enums.RuleCategory;
import com.market.analysis.model.enums.SignalStrength;
import com.market.analysis.model.enums.Trend;
import org.springframework.stereotype.Component;

@Component
public class PERatioRule implements AnalysisRule {
    private static final double CHEAP_PE = 15;
    private static final double EXPENSIVE_PE = 25;

    @Override
    public RuleCategory category() {
        return RuleCategory.VALUATION;
    }

    @Override
    public String name() {
        return "PE Ratio Valuation";
    }

    @Override
    public double weight() {
        return 1.0;
    }

    @Override
    public RuleEvaluation evaluate(StockMetrics metrics) {
        double pe = metrics.peRatio();
        if(pe <= 0) {
            return RuleEvaluation.neutral(category(), weight(), "PE ratio unavailable");
        }
        if(pe < CHEAP_PE) {
            return RuleEvaluation.bullish(85, SignalStrength.STRONG, category(), weight(), "Low PE ratio indicates potentially undervalued stock");
        }
        if(pe > EXPENSIVE_PE) {
            return RuleEvaluation.bearish(25, SignalStrength.WEAK, category(), weight(), "High PE ratio indicates expensive valuation");
        }
        return new RuleEvaluation(true, 60, Trend.NEUTRAL, SignalStrength.MODERATE, category(), weight(), "PE ratio within fair valuation range");
    }
}