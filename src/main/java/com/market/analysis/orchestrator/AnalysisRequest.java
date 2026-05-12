package com.market.analysis.orchestrator;

import java.util.List;
import java.util.Objects;

/**
 * Request to analyze stocks
 */
public record AnalysisRequest(
        List<String> symbols,
        boolean includeContext,
        boolean includeReport
) {
    public AnalysisRequest {
        Objects.requireNonNull(symbols);
        if (symbols.isEmpty()) {
            throw new IllegalArgumentException("At least one symbol required");
        }
    }
}