package com.market.analysis.rules;

import com.market.analysis.model.StockMetrics;
import com.market.analysis.model.enums.RuleCategory;
import com.market.analysis.model.enums.SignalStrength;
import org.springframework.stereotype.Component;

@Component
public class PriceAboveSMA200Rule implements AnalysisRule {
    @Override
    public RuleCategory category() {
        return RuleCategory.TREND;
    }

    @Override
    public String name() {
        return "Price Above SMA200";
    }

    @Override
    public double weight() {
        return 1.5;
    }

    @Override
    public RuleEvaluation evaluate(StockMetrics metrics) {
        if(metrics.sma200() <= 0) {
            return RuleEvaluation.neutral(category(), weight(), "SMA200 unavailable");
        }
        boolean matched = metrics.currentPrice() > metrics.sma200();
        if(matched) {
            return RuleEvaluation.bullish(85, SignalStrength.STRONG, category(), weight(), "Price trading above SMA200 indicates bullish long-term trend");
        }
        return RuleEvaluation.bearish(25, SignalStrength.WEAK, category(), weight(), "Price trading below SMA200 indicates bearish long-term trend");

    }
}