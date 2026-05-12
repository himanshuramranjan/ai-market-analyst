package com.market.analysis.aggregator.provider;

import com.market.analysis.aggregator.service.AlphaVantageIndicatorService;
import com.market.analysis.aggregator.service.HistoricalCandleService;
import com.market.analysis.exception.ApiException;
import com.market.analysis.indicator.IndicatorCalculationService;
import com.market.analysis.model.HistoricalCandle;
import com.market.analysis.model.StockMetrics;
import com.market.analysis.model.enums.DataSourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public class AlphaVantageProvider implements MarketDataProvider {

    private final HistoricalCandleService candleService;
    private final IndicatorCalculationService indicatorService;
    private final AlphaVantageIndicatorService indicatorApiService;

    @Override
    public Optional<StockMetrics> fetchStock(String symbol) {
        List<HistoricalCandle> candles = candleService.fetchCandles(symbol);
        if(candles.isEmpty()) {
            return Optional.empty();
        }

        HistoricalCandle latest = candles.get(candles.size() - 1);
        double sma50 = indicatorService.calculateSMA(candles, 50);
        /* Fetch remotely because compact candles * only contain ~100 data points */
        double sma200 = indicatorApiService.fetchSMA200(symbol);
        double rsi = indicatorService.calculateRSI(candles, 14);
        double avgVolume = indicatorService.calculateAverageVolume(candles, 20);
        StockMetrics metrics = new StockMetrics(symbol, symbol, latest.close(), candles.get(candles.size() - 2).close(), 0, rsi, sma50, sma200, latest.volume(), (long) avgVolume, latest.high(), latest.low(), calculate52WeekHigh(candles), calculate52WeekLow(candles), LocalDateTime.now(), getProviderName(), List.of());

        log.info("SMA50={}, SMA200={}, RSI={}", sma50, sma200, rsi);

        return Optional.of(metrics);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public DataSourceType getProviderName() {
        return DataSourceType.ALPHA_VANTAGE;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    private double calculate52WeekHigh(List<HistoricalCandle> candles) {
        return candles.stream().mapToDouble(HistoricalCandle::high).max().orElse(0);
    }

    private double calculate52WeekLow(List<HistoricalCandle> candles) {
        return candles.stream().mapToDouble(HistoricalCandle::low).min().orElse(0);
    }
}