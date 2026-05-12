package com.market.analysis.rules;

import com.market.analysis.model.StockMetrics;
import com.market.analysis.model.enums.RuleCategory;
import com.market.analysis.model.enums.SignalStrength;
import org.springframework.stereotype.Component;

@Component
public class PullbackFromHighRule implements AnalysisRule {

    @Override
    public RuleCategory category() {

        return RuleCategory.TREND;
    }

    @Override
    public String name() {

        return "Pullback From High Rule";
    }

    @Override
    public double weight() {

        return 1.2;
    }

    @Override
    public RuleEvaluation evaluate(StockMetrics metrics) {

        if(metrics.fiftyTwoWeekHigh() <= 0) {

            return RuleEvaluation.neutral(category(), weight(), "52-week high unavailable");
        }

        double pullbackPercent = ((metrics.fiftyTwoWeekHigh() - metrics.currentPrice()) / metrics.fiftyTwoWeekHigh()) * 100;

        if(pullbackPercent >= 5 && pullbackPercent <= 15) {

            return RuleEvaluation.bullish(80, SignalStrength.MODERATE, category(), weight(), "Healthy pullback from recent highs may provide accumulation opportunity");
        }

        if(pullbackPercent > 25) {

            return RuleEvaluation.bearish(35, SignalStrength.MODERATE, category(), weight(), "Deep correction indicates possible structural weakness");
        }

        return RuleEvaluation.neutral(category(), weight(), "No significant pullback opportunity detected");
    }
}