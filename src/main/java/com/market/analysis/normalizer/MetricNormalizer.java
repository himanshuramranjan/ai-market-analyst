package com.market.analysis.normalizer;

import com.market.analysis.model.StockMetrics;

public interface MetricNormalizer {

    /**
     * Normalize metrics to standard format
     * @param metrics raw metrics from provider
     * @return normalized metrics
     */
    StockMetrics normalize(StockMetrics metrics);
}