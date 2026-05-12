package com.market.analysis.aggregator.provider;

import com.market.analysis.model.StockMetrics;
import com.market.analysis.model.enums.DataSourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.market.analysis.model.enums.DataSourceType.MOCK_DATA;

/**
 * Mock provider for Stage 1 development
 * Eliminates dependency on external APIs
 */
@Slf4j
@Component
@Profile("dev")
public class MockMarketDataProvider implements MarketDataProvider {

    @Override
    public Optional<StockMetrics> fetchStock(String symbol) {

        log.info("Fetching mock stock data for {}", symbol);

        StockMetrics metrics = new StockMetrics(symbol, resolveCompanyName(symbol),

                1435.70,      // currentPrice
                1435.70,      // previousClose
                0.0,          // peRatio unavailable
                65.98,        // RSI
                1384.35,      // SMA50
                1433.80,      // SMA200
                2_500_000L,   // volume
                2_100_000L,   // avgVolume
                1442.95,      // dayHigh
                1417.55,      // dayLow
                1611.20,      // fiftyTwoWeekHigh
                1290.00,      // fiftyTwoWeekLow
                LocalDateTime.now(), MOCK_DATA, List.of());

        return Optional.of(metrics);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public DataSourceType getProviderName() {
        return MOCK_DATA;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    private String resolveCompanyName(String symbol) {

        return switch(symbol.toUpperCase()) {

            case "TCS" -> "Tata Consultancy Services";

            case "INFY" -> "Infosys";

            case "RELIANCE" -> "Reliance Industries";

            case "NIFTY50" -> "NIFTY 50 Index";

            default -> symbol;
        };
    }
}
