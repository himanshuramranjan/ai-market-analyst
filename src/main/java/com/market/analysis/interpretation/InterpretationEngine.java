package com.market.analysis.interpretation;

import com.market.analysis.model.enums.Trend;
import com.market.analysis.rules.RuleEvaluation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class InterpretationEngine {

    public String buildNarrative(List<RuleEvaluation> evaluations, double score, double confidence) {

        long bullishCount = evaluations.stream().filter(RuleEvaluation::matched).filter(evaluation -> evaluation.trend() == Trend.BULLISH).count();

        long bearishCount = evaluations.stream().filter(RuleEvaluation::matched).filter(evaluation -> evaluation.trend() == Trend.BEARISH).count();

        StringBuilder narrative = new StringBuilder();

        /**
         * Trend structure analysis
         */
        if(bullishCount > bearishCount + 1) {
            narrative.append("Technical structure remains constructive with bullish indicators dominating the current setup. ");

        } else if(bearishCount > bullishCount + 1) {
            narrative.append("Technical structure appears weak with bearish signals outweighing bullish confirmations. ");

        } else {
            narrative.append("Signals remain mixed with no strong directional conviction. ");
        }

        /**
         * Confidence interpretation
         */
        if(confidence >= 75) {
            narrative.append("Indicator alignment is strong, resulting in high confidence in the current analysis. ");

        } else if(confidence >= 50) {
            narrative.append("Some indicators support the setup, though conflicting signals remain present. ");

        } else {
            narrative.append("Low confidence due to conflicting or incomplete technical conditions. ");
        }

        /**
         * Score interpretation
         */
        if(score >= 80) {
            narrative.append("Current conditions appear favorable for long-term accumulation.");

        } else if(score >= 65) {
            narrative.append("Selective accumulation may be considered with disciplined position sizing.");

        } else if(score >= 50) {
            narrative.append("Waiting for stronger confirmation may improve entry quality.");

        } else {
            narrative.append("Current setup appears weak for fresh accumulation.");
        }

        return narrative.toString();
    }
}