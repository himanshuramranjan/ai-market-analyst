package com.market.analysis.rules;

import com.market.analysis.model.StockMetrics;
import com.market.analysis.model.enums.RuleCategory;
import com.market.analysis.model.enums.SignalStrength;
import org.springframework.stereotype.Component;

@Component
public class PriceNearSMA50Rule implements AnalysisRule {

    @Override
    public RuleCategory category() {

        return RuleCategory.TREND;
    }

    @Override
    public String name() {

        return "Price Near SMA50 Rule";
    }

    @Override
    public double weight() {

        return 1.1;
    }

    @Override
    public RuleEvaluation evaluate(StockMetrics metrics) {

        if(metrics.sma50() <= 0) {

            return RuleEvaluation.neutral(category(), weight(), "SMA50 unavailable");
        }

        double distance = Math.abs(metrics.currentPrice() - metrics.sma50()) / metrics.sma50() * 100;

        if(distance <= 3) {

            return RuleEvaluation.bullish(75, SignalStrength.MODERATE, category(), weight(), "Price trading close to SMA50 support zone");
        }

        return RuleEvaluation.neutral(category(), weight(), "Price extended from SMA50 support");
    }
}