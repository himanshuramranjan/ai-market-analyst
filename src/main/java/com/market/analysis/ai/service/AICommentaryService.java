package com.market.analysis.ai.service;

import com.market.analysis.ai.client.GroqClient;
import com.market.analysis.ai.model.AIAnalysisContext;
import com.market.analysis.ai.prompt.PromptBuilder;
import com.market.analysis.config.MarketAnalysisProperties;
import com.market.analysis.exception.AICommentaryException;
import com.market.analysis.model.*;
import com.market.analysis.model.enums.Trend;
import com.market.analysis.rules.RuleEvaluation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AICommentaryService {

    private final PromptBuilder promptBuilder;

    private final GroqClient groqClient;

    private final MarketAnalysisProperties properties;

    public String generateCommentary(StockMetrics metrics, TechnicalSignal signal, ScoreCard scoreCard, RecommendationResult recommendation, String interpretation) {

        try {

            List<String> bullishSignals = signal.evaluations().stream().filter(e -> e.trend() == Trend.BULLISH).map(RuleEvaluation::reason).toList();

            List<String> bearishSignals = signal.evaluations().stream().filter(e -> e.trend() == Trend.BEARISH).map(RuleEvaluation::reason).toList();

            List<String> neutralSignals = signal.evaluations().stream().filter(e -> e.trend() == Trend.NEUTRAL).map(RuleEvaluation::reason).toList();

            AIAnalysisContext context = new AIAnalysisContext(metrics.symbol(), metrics.currentPrice(), metrics.rsi(), metrics.sma50(), metrics.sma200(), signal.trendSignal().trend(), signal.momentumSignal().momentum(), signal.valuationSignal().valuation(), signal.trendSignal().trendScore(), signal.momentumSignal().momentumScore(), signal.valuationSignal().valuationScore(), scoreCard.compositeScore(), scoreCard.confidence(), recommendation.recommendation(), interpretation, bullishSignals, bearishSignals, neutralSignals);

            String prompt = promptBuilder.buildPrompt(context);

            log.info("Generating AI commentary for {}", metrics.symbol());

            String apiKey = properties.getAi().getGroq().getApiKey();

            return groqClient.generateCommentary(apiKey, prompt);

        } catch(Exception ex) {

            throw new AICommentaryException("Failed generating AI commentary", ex);
        }
    }
}