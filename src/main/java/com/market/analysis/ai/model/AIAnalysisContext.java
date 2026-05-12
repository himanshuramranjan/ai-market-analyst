package com.market.analysis.ai.model;

import com.market.analysis.model.enums.*;
import java.util.List;
public record AIAnalysisContext(
        String symbol,
        double currentPrice,
        double rsi,
        double sma50,
        double sma200,
        Trend trend,
        Momentum momentum,
        Valuation valuation,
        double trendScore,
        double momentumScore,
        double valuationScore,
        double compositeScore,
        double confidence,
        Recommendation recommendation,
        String interpretation,
        List<String> bullishSignals,
        List<String> bearishSignals,
        List<String> neutralSignals) {}
