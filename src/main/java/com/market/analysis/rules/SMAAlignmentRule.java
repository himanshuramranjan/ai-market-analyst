package com.market.analysis.rules;

import com.market.analysis.model.StockMetrics;
import com.market.analysis.model.enums.RuleCategory;
import com.market.analysis.model.enums.SignalStrength;
import com.market.analysis.model.enums.Trend;
import org.springframework.stereotype.Component;

@Component
public class SMAAlignmentRule implements AnalysisRule {

    @Override
    public RuleCategory category() {

        return RuleCategory.TREND;
    }

    @Override
    public String name() {

        return "SMA Alignment Rule";
    }

    @Override
    public double weight() {

        return 1.4;
    }

    @Override
    public RuleEvaluation evaluate(StockMetrics metrics) {

        if(metrics.sma50() <= 0 || metrics.sma200() <= 0) {

            return RuleEvaluation.neutral(category(), weight(), "Insufficient moving average data");
        }

        double price = metrics.currentPrice();

        boolean strongBullish = price > metrics.sma50() && metrics.sma50() > metrics.sma200();

        boolean recoveryBullish = price > metrics.sma200() && metrics.sma50() < metrics.sma200();

        if(strongBullish) {

            return RuleEvaluation.bullish(90, SignalStrength.STRONG, category(), weight(), "Strong bullish structure with full SMA alignment");
        }

        if(recoveryBullish) {

            return new RuleEvaluation(true, 60, Trend.NEUTRAL, SignalStrength.MODERATE, category(), weight(), "Recovery trend detected but SMA50 still below SMA200");
        }

        return RuleEvaluation.bearish(30, SignalStrength.STRONG, category(), weight(), "Weak long-term trend structure");
    }
}