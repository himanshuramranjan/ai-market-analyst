package com.market.analysis.rules;

import com.market.analysis.model.StockMetrics;
import com.market.analysis.model.enums.RuleCategory;
import com.market.analysis.model.enums.SignalStrength;
import org.springframework.stereotype.Component;

@Component
public class SMA50AboveSMA200Rule implements AnalysisRule {
    @Override
    public RuleCategory category() {
        return RuleCategory.TREND;
    }

    @Override
    public String name() {
        return "SMA50 Above SMA200";
    }

    @Override
    public double weight() {
        return 1.2;
    }

    @Override
    public RuleEvaluation evaluate(StockMetrics metrics) {
        boolean matched = metrics.sma50() > metrics.sma200();
        if(matched) {
            return RuleEvaluation.bullish(80, SignalStrength.STRONG, category(), weight(), "SMA50 above SMA200 confirms bullish trend structure");
        }
        return RuleEvaluation.bearish(30, SignalStrength.MODERATE, category(), weight(), "SMA50 below SMA200 indicates weak trend structure");
    }
}