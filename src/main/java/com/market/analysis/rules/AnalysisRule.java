package com.market.analysis.rules;

import com.market.analysis.model.StockMetrics;
import com.market.analysis.model.enums.RuleCategory;

public interface AnalysisRule { RuleCategory category(); String name(); double weight(); RuleEvaluation evaluate(StockMetrics metrics); }