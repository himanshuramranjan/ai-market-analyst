package com.market.analysis.rules;

import com.market.analysis.model.StockMetrics;
import com.market.analysis.model.enums.RuleCategory;
import com.market.analysis.model.enums.SignalStrength;
import com.market.analysis.model.enums.Trend;
import org.springframework.stereotype.Component;

@Component
public class RSIBullishMomentumRule implements AnalysisRule {
    @Override
    public RuleCategory category() {
        return RuleCategory.MOMENTUM;
    }

    @Override
    public String name() {
        return "RSI Bullish Momentum";
    }

    @Override
    public double weight() {
        return 1.0;
    }

    @Override
    public RuleEvaluation evaluate(StockMetrics metrics) {
        double rsi = metrics.rsi();

        if(rsi >= 70) {

            return RuleEvaluation.bearish(65, SignalStrength.MODERATE, category(), weight(), "RSI above 70 indicates overbought momentum");
        }

        if(rsi >= 60) {

            return RuleEvaluation.bullish(80, SignalStrength.STRONG, category(), weight(), "RSI above 60 indicates strong bullish momentum");
        }

        if(rsi <= 40) {

            return RuleEvaluation.bearish(25, SignalStrength.WEAK, category(), weight(), "RSI below 40 indicates weak momentum");
        }

        return new RuleEvaluation(true, 50, Trend.NEUTRAL, SignalStrength.MODERATE, category(), weight(), "RSI in neutral zone");
    }
}