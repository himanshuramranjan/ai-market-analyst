package com.market.analysis.aggregator.service;


import com.market.analysis.aggregator.provider.MarketDataProvider;
import com.market.analysis.exception.ApiException;
import com.market.analysis.model.StockMetrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Orchestrates multiple providers with fallback mechanism
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiAggregatorService {

    private final List<MarketDataProvider> providers;

    /**
     * Fetch stock metrics from available providers
     * Tries providers in priority order
     *
     * @param symbol stock symbol
     * @return StockMetrics if any provider succeeds
     * @throws ApiException if all providers fail
     */
    public StockMetrics fetchStock(String symbol) throws Exception {
        log.info("Aggregating metrics for {}", symbol);

        List<String> failureReasons = new ArrayList<>();

        // Get providers ordered by priority
        List<MarketDataProvider> orderedProviders = providers.stream().filter(MarketDataProvider::isAvailable).sorted(Comparator.comparingInt(MarketDataProvider::getPriority)).collect(Collectors.toList());

        if(orderedProviders.isEmpty()) {
            throw new ApiException("No market data providers available");
        }

        for(MarketDataProvider provider : orderedProviders) {
            try {
                Optional<StockMetrics> metrics = provider.fetchStock(symbol);

                if(metrics.isPresent()) {
                    log.info("Successfully fetched {} from {}", symbol, provider.getProviderName());
                    return metrics.get();
                }

            } catch(Exception e) {
                failureReasons.add(provider.getProviderName() + ": " + e.getMessage());
                log.error("Provider {} failed for {}: {}", provider.getProviderName(), symbol, e);
            }
        }

        String errorMessage = String.format("All providers failed for %s: %s", symbol, String.join("; ", failureReasons));

        throw new ApiException(errorMessage);
    }
}