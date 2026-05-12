package com.market.analysis.orchestrator;

import com.market.analysis.aggregator.service.ApiAggregatorService;
import com.market.analysis.ai.service.AICommentaryService;
import com.market.analysis.interpretation.InterpretationEngine;
import com.market.analysis.model.*;
import com.market.analysis.normalizer.MetricNormalizer;
import com.market.analysis.reccomendation.RecommendationEngine;
import com.market.analysis.rules.engine.RuleEngine;
import com.market.analysis.scoring.ScoringEngine;
import com.market.analysis.validator.MetricValidator;
import com.market.analysis.validator.ValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Main orchestrator for analysis pipeline
 * Coordinates all layers: fetch -> validate -> normalize -> analyze -> score
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisOrchestrator {

    private final ApiAggregatorService apiAggregator;
    private final MetricValidator validator;
    private final MetricNormalizer normalizer;
    private final RuleEngine ruleEngine;
    private final ScoringEngine scoringEngine;
    private final InterpretationEngine interpretationEngine;
    private final RecommendationEngine recommendationEngine;
    private final AICommentaryService aiCommentaryService;

    /**
     * Execute complete analysis pipeline for multiple stocks
     * Per-stock error isolation: failures in one stock don't affect others
     *
     * @param request analysis request
     * @return list of results (success or error)
     */
    public List<AnalysisResult> analyzePortfolio(AnalysisRequest request) {
        log.info("Starting portfolio analysis for {} stocks", request.symbols().size());

        List<AnalysisResult> results = new ArrayList<>();

        // Analyze each stock independently
        for(String symbol : request.symbols()) {
            try {
                AnalysisResult result = analyzeStock(symbol);
                results.add(result);
            } catch(Exception e) {
                log.error("Unexpected error analyzing {}: {}", symbol, e.getMessage(), e);
                results.add(AnalysisResult.failed(symbol, "Unexpected error: " + e.getMessage(), AnalysisResult.AnalysisStatus.UNKNOWN_ERROR));
            }
        }

        log.info("Portfolio analysis complete: {} successful, {} failed", results.stream().filter(r -> r.status() == AnalysisResult.AnalysisStatus.SUCCESS).count(), results.stream().filter(r -> r.status() != AnalysisResult.AnalysisStatus.SUCCESS).count());

        return results;
    }

    /**
     * Analyze single stock through complete pipeline
     */
    private AnalysisResult analyzeStock(String symbol) {
        log.info("=== Analyzing {} ===", symbol);

        try {
            // Step 1: Fetch
            log.debug("Step 1: Fetching metrics for {}", symbol);
            StockMetrics metrics = apiAggregator.fetchStock(symbol);
            log.info("Fetched: price={}, PE={}, RSI={}", metrics.currentPrice(), metrics.peRatio(), metrics.rsi());

            // Step 2: Validate
            log.debug("Step 2: Validating metrics");
            ValidationResult validationResult = validator.validate(metrics);
            if(!validationResult.valid()) {
                String error = String.join("; ", validationResult.errors());
                log.error("Validation failed: {}", error);
                return AnalysisResult.failed(symbol, error, AnalysisResult.AnalysisStatus.VALIDATION_FAILED);
            }

            // Step 3: Normalize
            log.debug("Step 3: Normalizing metrics");
            StockMetrics normalized = normalizer.normalize(metrics);

            // Step 4: Analyze
            log.debug("Step 4: Running rule engine");
            TechnicalSignal signal = ruleEngine.analyze(normalized);

            // Step 5: Score
            log.debug("Step 5: Calculating scores");
            ScoreCard scoreCard = scoringEngine.calculate(signal.trendSignal(), signal.momentumSignal(), signal.valuationSignal());

            // Step 6: Interpretation
            log.debug("Step 6: Interpretating");
            String interpretation = interpretationEngine.buildNarrative(signal.evaluations(), scoreCard.compositeScore(), scoreCard.confidence());

            // Step 7: Recommendation
            log.debug("Step 7: Recommending");
            RecommendationResult recommendation = recommendationEngine.generateRecommendation(signal, scoreCard.compositeScore(), scoreCard.confidence());

            // Step 8: AI Commentary
            log.debug("Step 8: Generating AI commentary");
            String aiCommentary = aiCommentaryService.generateCommentary(normalized, signal, scoreCard, recommendation, interpretation);

            // Build result
            AnalysisResult result = AnalysisResult.success(symbol, normalized, signal, scoreCard, interpretation, recommendation, aiCommentary);

            log.info("✓ Successfully analyzed {}: score={}, confidence={}%", symbol, String.format("%.2f", result.scoreCard().compositeScore()), String.format("%.1f", scoreCard.confidence()));

            return result;

        } catch(Exception e) {
            log.error("API error for {}: {}", symbol, e.getMessage());
            return AnalysisResult.failed(symbol, e.getMessage(), AnalysisResult.AnalysisStatus.FETCH_FAILED);
        }
    }
}