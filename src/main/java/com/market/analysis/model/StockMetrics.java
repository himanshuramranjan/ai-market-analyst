package com.market.analysis.model;

import com.market.analysis.model.enums.DataSourceType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public record StockMetrics(
        String symbol,
        String companyName,
        double currentPrice,
        double previousClose,
        double peRatio,
        double rsi,
        double sma50,
        double sma200,
        double volume,
        double avgVolume,
        double dayHigh,
        double dayLow,
        double fiftyTwoWeekHigh,
        double fiftyTwoWeekLow,
        LocalDateTime fetchedAt,
        DataSourceType source,
        List<String> warnings
) {
    public StockMetrics {
        Objects.requireNonNull(symbol, "Symbol cannot be null");
        Objects.requireNonNull(fetchedAt, "FetchedAt cannot be null");
        Objects.requireNonNull(source, "Source cannot be null");
        Objects.requireNonNull(warnings, "Warnings cannot be null");
    }

    public boolean isPriceAboveSMA200() {
        return currentPrice > sma200;
    }

    public boolean isPriceAboveSMA50() {
        return currentPrice > sma50;
    }

    public boolean isSMA50AboveSMA200() {
        return sma50 > sma200;
    }

    public double getPercentFromSMA50() {
        if (sma50 == 0) return 0;
        return ((currentPrice - sma50) / sma50) * 100;
    }

    public double getPercentFromSMA200() {
        if (sma200 == 0) return 0;
        return ((currentPrice - sma200) / sma200) * 100;
    }

    public double getFiftyTwoWeekPosition() {
        if (fiftyTwoWeekHigh == fiftyTwoWeekLow) return 50;
        return ((currentPrice - fiftyTwoWeekLow) /
                (fiftyTwoWeekHigh - fiftyTwoWeekLow)) * 100;
    }
}