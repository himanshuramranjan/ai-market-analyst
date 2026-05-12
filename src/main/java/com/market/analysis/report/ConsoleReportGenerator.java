package com.market.analysis.report;

import com.market.analysis.orchestrator.AnalysisResult;
import com.market.analysis.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Generates console reports for analysis results
 */
@Slf4j
@Service
@Profile("dev")
public class ConsoleReportGenerator {

    /**
     * Print analysis results to console
     */
    public void printPortfolioReport(List<AnalysisResult> results) {
        printHeader("PORTFOLIO ANALYSIS REPORT");

        for(AnalysisResult result : results) {
            if(result.status() == AnalysisResult.AnalysisStatus.SUCCESS) {
                printSuccessReport(result);
            } else {
                printErrorReport(result);
            }
            printSeparator();
        }

        printSummary(results);
    }

    private void printSuccessReport(AnalysisResult result) {
        StockMetrics metrics = result.metrics();
        TechnicalSignal signal = result.signal();
        ScoreCard scoreCard = result.scoreCard();

        System.out.println("✓ " + metrics.symbol() + " - " + metrics.companyName());
        System.out.println();

        // Price Information
        System.out.println("PRICE INFORMATION:");
        System.out.printf("  Current Price:     ₹%.2f%n", metrics.currentPrice());
        System.out.printf("  Previous Close:    ₹%.2f%n", metrics.previousClose());
        System.out.printf("  Day High/Low:      ₹%.2f / ₹%.2f%n", metrics.dayHigh(), metrics.dayLow());
        System.out.printf("  52-Week Range:     ₹%.2f - ₹%.2f%n", metrics.fiftyTwoWeekLow(), metrics.fiftyTwoWeekHigh());
        System.out.println();

        // Technical Signals
        System.out.println("TECHNICAL ANALYSIS:");
        System.out.println("  Trend: " + signal.trendSignal().trend());
        signal.trendSignal().reasoning().forEach(r -> System.out.println("    • " + r));
        System.out.println("  Momentum: " + signal.momentumSignal().momentum());
        signal.momentumSignal().reasoning().forEach(r -> System.out.println("    • " + r));
        System.out.println("  Valuation: " + signal.valuationSignal().valuation());
        signal.valuationSignal().reasoning().forEach(r -> System.out.println("    • " + r));
        System.out.println();

        // Scores
        System.out.println("SCORES:");
        System.out.printf("  Trend Score:       %.2f / 100%n", scoreCard.trendScore());
        System.out.printf("  Momentum Score:    %.2f / 100%n", scoreCard.momentumScore());
        System.out.printf("  Valuation Score:   %.2f / 100%n", scoreCard.valuationScore());
        System.out.println();
        System.out.printf("  Composite Score:   %.2f / 100%n", scoreCard.compositeScore());
        System.out.printf("  Confidence:        %.1f%% (%s)%n", scoreCard.confidence(), scoreCard.confidenceLevel());
        System.out.println();

        // Interpretation
        System.out.println("INTERPRETATION:");
        System.out.println("  " + result.interpretation());

        System.out.println();

        // Recommendation
        System.out.println("RECOMMENDATION:");
        System.out.println("  " + result.recommendation().recommendation());

        System.out.println();

        System.out.println("REASON:");
        System.out.println("  " + result.recommendation().reason());

        System.out.println();

        System.out.println("AI COMMENTARY:");
        System.out.println(" " + result.aiCommentary());

    }

    private void printErrorReport(AnalysisResult result) {
        System.out.println("✗ " + result.symbol() + " - FAILED");
        System.out.println("  Status: " + result.status());
        System.out.println("  Error: " + result.error());
    }

    private void printSummary(List<AnalysisResult> results) {
        printHeader("SUMMARY");

        long successCount = results.stream().filter(r -> r.status() == AnalysisResult.AnalysisStatus.SUCCESS).count();

        long failedCount = results.size() - successCount;

        System.out.printf("Total Stocks:      %d%n", results.size());
        System.out.printf("Successful:        %d%n", successCount);
        System.out.printf("Failed:            %d%n", failedCount);
        System.out.println();

        if(successCount > 0) {
            System.out.println("SUCCESS STOCKS:");
            results.stream().filter(r -> r.status() == AnalysisResult.AnalysisStatus.SUCCESS).forEach(r -> System.out.printf("  %-10s Score: %.2f, Confidence: %.1f%%%n", r.symbol(), r.scoreCard().compositeScore(), r.scoreCard().confidence()));
        }

        if(failedCount > 0) {
            System.out.println("FAILED STOCKS:");
            results.stream().filter(r -> r.status() != AnalysisResult.AnalysisStatus.SUCCESS).forEach(r -> System.out.printf("  %-10s %s%n", r.symbol(), r.error()));
        }
    }

    private void printHeader(String title) {
        System.out.println();
        System.out.println("====================================");
        System.out.println(title);
        System.out.println("====================================");
    }

    private void printSeparator() {
        System.out.println("------------------------------------");
    }
}