package com.market.analysis.model;

import com.market.analysis.model.enums.Recommendation;

public record RecommendationResult(Recommendation recommendation, String reason ) {}