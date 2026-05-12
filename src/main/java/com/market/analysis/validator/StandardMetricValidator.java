package com.market.analysis.validator;

import com.market.analysis.exception.MarketDataException;
import com.market.analysis.model.StockMetrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Standard implementation of metric validator
 */
@Slf4j
@Component
public class StandardMetricValidator implements MetricValidator {

    private static final double EPSILON = 0.0001;

    @Override
    public ValidationResult validate(StockMetrics metrics) {
        log.debug("Validating metrics for {}", metrics.symbol());

        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        // Critical validations (errors)
        validatePrice(metrics, errors);
        validateRSI(metrics, errors);
        validateMovingAverages(metrics, errors);
        validateVolume(metrics, errors);
        validateFiftyTwoWeekRange(metrics, errors);

        // Warning validations (non-critical)
        validatePERatio(metrics, warnings);
        validateDataCompleteness(metrics, warnings);

        if(!errors.isEmpty()) {
            throw new MarketDataException("Insufficient historical candles for analysis, Error(s) : " + errors);
        }

        if(!warnings.isEmpty()) {
            log.warn("Validation warnings for {}: {}", metrics.symbol(), warnings);
            return ValidationResult.withWarnings(warnings);
        }

        log.debug("Validation passed for {}", metrics.symbol());
        return ValidationResult.success();
    }

    private void validatePrice(StockMetrics metrics, List<String> errors) {
        if(metrics.currentPrice() <= EPSILON) {
            errors.add("Current price must be positive");
        }
        if(metrics.previousClose() < 0) {
            errors.add("Previous close cannot be negative");
        }
        if(metrics.dayHigh() > 0 && metrics.dayLow() > 0 && metrics.dayHigh() < metrics.dayLow()) {
            errors.add("Day high must be >= day low");
        }
    }

    private void validateRSI(StockMetrics metrics, List<String> errors) {
        if(metrics.rsi() > 0) {  // Only validate if present
            if(metrics.rsi() < 0 || metrics.rsi() > 100) {
                errors.add("RSI must be between 0 and 100");
            }
        }
    }

    private void validateMovingAverages(StockMetrics metrics, List<String> errors) {
        if(metrics.sma50() < 0 || metrics.sma200() < 0) {
            errors.add("Moving averages cannot be negative");
        }
    }

    private void validateVolume(StockMetrics metrics, List<String> errors) {
        if(metrics.volume() < 0 || metrics.avgVolume() < 0) {
            errors.add("Volume cannot be negative");
        }
    }

    private void validateFiftyTwoWeekRange(StockMetrics metrics, List<String> errors) {
        if(metrics.fiftyTwoWeekHigh() > 0 && metrics.fiftyTwoWeekLow() > 0) {
            if(metrics.fiftyTwoWeekHigh() < metrics.fiftyTwoWeekLow()) {
                errors.add("52-week high must be >= low");
            }
        }
    }

    private void validatePERatio(StockMetrics metrics, List<String> warnings) {
        if(metrics.peRatio() > 0 && metrics.peRatio() > 100) {
            warnings.add("Unusual high PE ratio: " + metrics.peRatio());
        }
        if(metrics.peRatio() < 0) {
            warnings.add("Negative PE ratio indicates losses");
        }
    }

    private void validateDataCompleteness(StockMetrics metrics, List<String> warnings) {
        if(metrics.sma50() == 0 || metrics.sma200() == 0) {
            warnings.add("Moving averages data incomplete");
        }
        if(metrics.rsi() == 0) {
            warnings.add("RSI data not available");
        }
    }
}