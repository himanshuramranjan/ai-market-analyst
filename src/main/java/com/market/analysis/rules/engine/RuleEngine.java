package com.market.analysis.rules.engine;

import com.market.analysis.model.StockMetrics;
import com.market.analysis.model.TechnicalSignal;

public interface RuleEngine {
    TechnicalSignal analyze(StockMetrics metrics);
}