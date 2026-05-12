package com.market.analysis.rules.engine;

import com.market.analysis.model.*;
import com.market.analysis.model.enums.*;
import com.market.analysis.rules.AnalysisRule;
import com.market.analysis.rules.RuleEvaluation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StandardRuleEngine implements RuleEngine {

    private final List<AnalysisRule> rules;

    @Override
    public TechnicalSignal analyze(StockMetrics metrics) {

        log.info("Running rule engine for {}", metrics.symbol());

        /**
         * Execute all rules ONCE
         */
        List<RuleEvaluation> evaluations = rules.stream().map(rule -> rule.evaluate(metrics)).toList();

        TrendSignal trendSignal = buildTrendSignal(evaluations);

        MomentumSignal momentumSignal = buildMomentumSignal(evaluations, metrics);

        ValuationSignal valuationSignal = buildValuationSignal(evaluations, metrics);

        return new TechnicalSignal(metrics.symbol(), trendSignal, momentumSignal, valuationSignal, evaluations);
    }

    private TrendSignal buildTrendSignal(List<RuleEvaluation> evaluations) {

        List<RuleEvaluation> trendRules = filterByCategory(evaluations, RuleCategory.TREND);

        double score = calculateDirectionalScore(trendRules);

        Trend trend = determineTrend(score);

        SignalStrength strength = determineStrength(score);

        List<String> reasoning = trendRules.stream().map(RuleEvaluation::reason).toList();

        return new TrendSignal(trend, score, reasoning, strength);
    }

    private MomentumSignal buildMomentumSignal(List<RuleEvaluation> evaluations, StockMetrics metrics) {

        List<RuleEvaluation> momentumRules = filterByCategory(evaluations, RuleCategory.MOMENTUM);

        double score = calculateDirectionalScore(momentumRules);

        Momentum momentum = determineMomentum(score);

        SignalStrength strength = determineStrength(score);

        List<String> reasoning = momentumRules.stream().map(RuleEvaluation::reason).toList();

        return new MomentumSignal(momentum, score, reasoning, strength, metrics.rsi());
    }

    private ValuationSignal buildValuationSignal(List<RuleEvaluation> evaluations, StockMetrics metrics) {

        List<RuleEvaluation> valuationRules = filterByCategory(evaluations, RuleCategory.VALUATION);

        double score = calculateDirectionalScore(valuationRules);

        Valuation valuation = determineValuation(score);

        SignalStrength strength = determineStrength(score);

        List<String> reasoning = valuationRules.stream().map(RuleEvaluation::reason).toList();

        return new ValuationSignal(valuation, score, reasoning, strength, metrics.peRatio());
    }

    private List<RuleEvaluation> filterByCategory(List<RuleEvaluation> evaluations, RuleCategory category) {
        return evaluations.stream().filter(evaluation -> evaluation.category() == category).toList();
    }

    private double calculateDirectionalScore(List<RuleEvaluation> evaluations) {

        if(evaluations.isEmpty()) {
            return 50;
        }

        double weightedDirectionalSum = 0;
        double totalWeight = 0;

        for(RuleEvaluation evaluation : evaluations) {

            double directionalValue = switch(evaluation.trend()) {

                case BULLISH -> 1.0;

                case BEARISH -> -1.0;

                case NEUTRAL -> 0.0;
            };

            /**
             * Convert score strength into confidence multiplier
             */
            double normalizedStrength = evaluation.score() / 100.0;

            double contribution = directionalValue * normalizedStrength * evaluation.weight();

            weightedDirectionalSum += contribution;

            totalWeight += evaluation.weight();
        }

        /**
         * Normalize:
         * -1 → 0
         *  0 → 50
         * +1 → 100
         */
        double normalized = weightedDirectionalSum / totalWeight;

        return ((normalized + 1) / 2.0) * 100;
    }

    private Trend determineTrend(double score) {

        if(score >= 65) {
            return Trend.BULLISH;
        }

        if(score <= 35) {
            return Trend.BEARISH;
        }

        return Trend.NEUTRAL;
    }

    private Momentum determineMomentum(double score) {

        if(score >= 70) {
            return Momentum.STRONG;
        }

        if(score <= 40) {
            return Momentum.WEAK;
        }

        return Momentum.NEUTRAL;
    }

    private Valuation determineValuation(double score) {

        if(score >= 70) {
            return Valuation.CHEAP;
        }

        if(score <= 40) {
            return Valuation.EXPENSIVE;
        }

        return Valuation.FAIR;
    }

    private SignalStrength determineStrength(double score) {

        if(score >= 85) {
            return SignalStrength.VERY_STRONG;
        }

        if(score >= 70) {
            return SignalStrength.STRONG;
        }

        if(score >= 50) {
            return SignalStrength.MODERATE;
        }

        return SignalStrength.WEAK;
    }
}