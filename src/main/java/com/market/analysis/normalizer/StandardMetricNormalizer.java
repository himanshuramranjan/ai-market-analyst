package com.market.analysis.normalizer;

import com.market.analysis.model.StockMetrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Standard metric normalization
 * - Rounds to 2 decimal places
 * - Removes null/NaN values
 * - Ensures consistency across providers
 */
@Slf4j
@Component
public class StandardMetricNormalizer implements MetricNormalizer {

    private static final int DECIMAL_PLACES = 2;

    @Override
    public StockMetrics normalize(StockMetrics metrics) {
        log.debug("Normalizing metrics for {}", metrics.symbol());

        return new StockMetrics(
                metrics.symbol(),
                metrics.companyName(),
                round(metrics.currentPrice()),
                round(metrics.previousClose()),
                round(metrics.peRatio()),
                round(metrics.rsi()),
                round(metrics.sma50()),
                round(metrics.sma200()),
                round(metrics.volume()),
                round(metrics.avgVolume()),
                round(metrics.dayHigh()),
                round(metrics.dayLow()),
                round(metrics.fiftyTwoWeekHigh()),
                round(metrics.fiftyTwoWeekLow()),
                metrics.fetchedAt(),
                metrics.source(),
                metrics.warnings()
        );
    }

    private double round(double value) {
        if (value == 0) return 0;
        long factor = (long) Math.pow(10, DECIMAL_PLACES);
        return (double) Math.round(value * factor) / factor;
    }
}
