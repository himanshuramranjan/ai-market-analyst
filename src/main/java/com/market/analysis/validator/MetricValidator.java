package com.market.analysis.validator;

import com.market.analysis.model.StockMetrics;

public interface MetricValidator {

    /**
     * Validate metrics for consistency and ranges
     * @param metrics metrics to validate
     * @return validation result with errors/warnings
     */
    ValidationResult validate(StockMetrics metrics);
}
