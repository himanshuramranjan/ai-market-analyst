package com.market.analysis.orchestrator;

import com.market.analysis.model.*;
import java.util.List;
import java.util.Objects;

/**
 * Complete analysis result for a single stock
 */
public record AnalysisResult(
        String symbol,
        StockMetrics metrics,
        TechnicalSignal signal,
        ScoreCard scoreCard,
        String interpretation,
        RecommendationResult recommendation,
        AnalysisStatus status,
        String aiCommentary,
        String error
) {
    public AnalysisResult {
        Objects.requireNonNull(symbol);
        Objects.requireNonNull(status);
    }

    public boolean successful() { return status == AnalysisStatus.SUCCESS; }

    public enum AnalysisStatus {
        SUCCESS,
        FETCH_FAILED,
        VALIDATION_FAILED,
        ANALYSIS_FAILED,
        UNKNOWN_ERROR
    }

    public static AnalysisResult success(
            String symbol,
            StockMetrics metrics,
            TechnicalSignal signal,
            ScoreCard scoreCard,
            String interpretation,
            RecommendationResult recommendation,
            String aiCommentary
    ) {
        return new AnalysisResult( symbol, metrics, signal, scoreCard, interpretation, recommendation, AnalysisStatus.SUCCESS, aiCommentary, null);
    }

    public static AnalysisResult failed(String symbol, String error, AnalysisStatus status) {
        return new AnalysisResult( symbol, null, null, null, null, null, status, null, error);
    }
}