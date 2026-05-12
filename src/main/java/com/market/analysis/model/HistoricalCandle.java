package com.market.analysis.model;

import java.time.LocalDate;

public record HistoricalCandle(LocalDate date, double open, double high, double low, double close, long volume) {
}
