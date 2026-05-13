package com.market.analysis.aggregator.service;

import com.market.analysis.aggregator.dto.AlphaVantageResponse;
import com.market.analysis.aggregator.ratelimit.ApiRateLimiter;
import com.market.analysis.config.MarketAnalysisProperties;
import com.market.analysis.exception.ApiException;
import com.market.analysis.model.HistoricalCandle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlphaVantageHistoricalCandleService implements HistoricalCandleService {
    private final WebClient webClient;
    private final MarketAnalysisProperties properties;
    private final ApiRateLimiter apiRateLimiter;

    @Override
    public List<HistoricalCandle> fetchCandles(String symbol) {
        try {

            log.info("Fetching candles for symbol={} using AlphaVantage", symbol);

            String apiKey = properties.getApi().getAlphaVantage().getApiKey();
            String baseUrl = properties.getApi().getAlphaVantage().getBaseUrl();
            String formattedSymbol = symbol + ".BSE";
            String url = baseUrl + "/query" + "?function=TIME_SERIES_DAILY" + "&symbol=" + formattedSymbol + "&outputsize=compact" + "&apikey=" + apiKey;

            log.info("Fetching historical candles for {}", symbol);
            log.info("Waiting before making request to avoid rate limit");

            apiRateLimiter.acquire(); // to limit the api calls according to free tier of alphaVantage

            AlphaVantageResponse response = webClient.get().uri(url).retrieve().bodyToMono(AlphaVantageResponse.class).timeout(Duration.ofSeconds(20)).block();

            return response.getTimeSeries().entrySet().stream().map(entry -> {
                AlphaVantageResponse.DailyData d = entry.getValue();

                return new HistoricalCandle(LocalDate.parse(entry.getKey()), parse(d.open()), parse(d.high()), parse(d.low()), parse(d.close()), Long.parseLong(d.volume()));
            }).sorted(Comparator.comparing(HistoricalCandle::date)).toList();
        } catch(Exception ex) {
            log.error("Error in fetching the candle details : ", ex);
            throw new ApiException("Failed fetching market data from AlphaVantage", ex);
        }
    }

    private double parse(String value) {
        return value == null ? 0 : Double.parseDouble(value);
    }
}
