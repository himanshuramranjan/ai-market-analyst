package com.market.analysis.aggregator.provider;

import com.market.analysis.model.StockMetrics;
import com.market.analysis.model.enums.DataSourceType;

import java.util.Optional;


public interface MarketDataProvider {

    /**
     * Fetch stock metrics from this provider
     *
     * @param symbol stock symbol (e.g., "RELIANCE")
     * @return metrics if available, empty otherwise
     */
    Optional<StockMetrics> fetchStock(String symbol);

    /**
     * Check provider availability
     */
    boolean isAvailable();

    /**
     * Provider identifier
     */
    DataSourceType getProviderName();

    /**
     * Provider priority (lower = higher priority in fallback chain)
     */
    int getPriority();
}