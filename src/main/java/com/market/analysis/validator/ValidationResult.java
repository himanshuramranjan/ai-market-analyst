package com.market.analysis.validator;

import java.util.List;
import java.util.Objects;

public record ValidationResult(boolean valid, List<String> errors, List<String> warnings) {
    public ValidationResult {
        Objects.requireNonNull(errors);
        Objects.requireNonNull(warnings);
    }

    public static ValidationResult success() {
        return new ValidationResult(true, List.of(), List.of());
    }

    public static ValidationResult withWarnings(List<String> warnings) {
        return new ValidationResult(true, List.of(), warnings);
    }

    public static ValidationResult failed(List<String> errors) {
        return new ValidationResult(false, errors, List.of());
    }
}