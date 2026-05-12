package com.market.analysis.rules;

import com.market.analysis.model.StockMetrics;
import com.market.analysis.model.enums.RuleCategory;
import com.market.analysis.model.enums.SignalStrength;
import org.springframework.stereotype.Component;

@Component
public class RSICoolingRule implements AnalysisRule {

    @Override
    public RuleCategory category() {

        return RuleCategory.MOMENTUM;
    }

    @Override
    public String name() {

        return "RSI Cooling Rule";
    }

    @Override
    public double weight() {

        return 1.1;
    }

    @Override
    public RuleEvaluation evaluate(StockMetrics metrics) {

        double rsi = metrics.rsi();

        if(rsi <= 0) {

            return RuleEvaluation.neutral(category(), weight(), "RSI unavailable");
        }

        if(rsi >= 45 && rsi <= 60) {

            return RuleEvaluation.bullish(75, SignalStrength.MODERATE, category(), weight(), "RSI indicates healthy momentum reset");
        }

        if(rsi > 60 && rsi <= 75) {

            return RuleEvaluation.bullish(85, SignalStrength.STRONG, category(), weight(), "RSI indicates strong bullish momentum");
        }

        if(rsi > 75) {

            return RuleEvaluation.bearish(35, SignalStrength.MODERATE, category(), weight(), "RSI indicates overheated momentum conditions");
        }

        if(rsi < 35) {

            return RuleEvaluation.bearish(40, SignalStrength.MODERATE, category(), weight(), "RSI indicates weak momentum");
        }

        return RuleEvaluation.neutral(category(), weight(), "Momentum conditions remain neutral");
    }
}