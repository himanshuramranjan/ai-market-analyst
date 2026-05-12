package com.market.analysis.ai.prompt;

import com.market.analysis.ai.model.AIAnalysisContext;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class PromptBuilder {

    public String buildPrompt(AIAnalysisContext context) {

        String analysisTimestamp = ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).format(DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a"));

        return """
                You are an experienced Indian stock market analyst.

                Analysis Timestamp:
                %s IST
                                
                Your task is to provide a concise and practical commentary
                focused specifically on whether the current setup provides
                a reasonable accumulation / entry or exit opportunity for the stock.
                                
                Base your analysis primarily on the structured technical
                signals and scores provided below.
                                
                Use the provided deterministic analysis as the primary source
                of truth.
                                
                You may briefly consider the broader Indian market environment
                ONLY if it materially affects the reliability of the current
                technical setup.
                                
                If broader market conditions could materially influence the
                reliability of the current setup, mention that as part of
                the commentary.
                                
                Do not assume exact live market conditions unless strongly
                implied by the provided technical structure.
                                
                Focus on:
                                
                > entry quality
                > trend structure
                > momentum quality
                > signal alignment
                > accumulation suitability
                > near-term technical risk
                                
                Avoid:
                                
                > long-term business analysis
                > revenue or earnings discussions
                > target price predictions
                > generic investment advice
                > generic market commentary
                > excessive optimism or pessimism
                > repeating raw metrics unnecessarily
                                
                Do NOT invent indicators.
                Use ONLY the provided information.
                                
                The response should:
                                
                > be extremely concise
                > be limited to 3-5 short sentences maximum
                > stay under 100 words
                > avoid paragraphs or bullet points
                > sound professional and insight-driven
                > focus only on practical entry / exit quality
                > avoid repeating raw metrics
                > avoid generic financial commentary
                > avoid excessive explanation

                =========================================

                Symbol: %s

                Current Price: %.2f
                RSI: %.2f
                SMA50: %.2f
                SMA200: %.2f

                Trend: %s
                Momentum: %s
                Valuation: %s

                Trend Score: %.2f
                Momentum Score: %.2f
                Valuation Score: %.2f

                Composite Score: %.2f
                Confidence: %.2f

                Recommendation: %s

                Deterministic Interpretation:
                %s

                Bullish Signals:
                %s

                Bearish Signals:
                %s

                Neutral Signals:
                %s
                """.formatted(analysisTimestamp, context.symbol(), context.currentPrice(), context.rsi(), context.sma50(), context.sma200(), context.trend(), context.momentum(), context.valuation(), context.trendScore(), context.momentumScore(), context.valuationScore(), context.compositeScore(), context.confidence(), context.recommendation(), context.interpretation(), context.bullishSignals(), context.bearishSignals(), context.neutralSignals());
    }
}