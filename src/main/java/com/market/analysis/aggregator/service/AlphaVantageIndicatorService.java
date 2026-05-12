package com.market.analysis.aggregator.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.analysis.aggregator.ratelimit.ApiRateLimiter;
import com.market.analysis.config.MarketAnalysisProperties;
import com.market.analysis.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlphaVantageIndicatorService {

    private final WebClient webClient;

    private final MarketAnalysisProperties properties;
    private final ApiRateLimiter apiRateLimiter;

    /**
     * Fetch SMA200 directly from AlphaVantage
     */
    public double fetchSMA200(String symbol) {

        try {

            String apiKey = properties.getApi().getAlphaVantage().getApiKey();

            String baseUrl = properties.getApi().getAlphaVantage().getBaseUrl();

            String formattedSymbol = symbol.endsWith(".BSE") ? symbol : symbol + ".BSE";

            String url = baseUrl + "/query" + "?function=SMA" + "&symbol=" + formattedSymbol + "&interval=daily" + "&time_period=200" + "&series_type=close" + "&apikey=" + apiKey;

            log.info("Fetching SMA200 for {}", url);

            log.info("Waiting before making request to avoid rate limit");

            apiRateLimiter.acquire(); // to limit the api calls according to free tier of alphaVantage
            String rawResponse = webClient.get().uri(url).retrieve().bodyToMono(String.class).timeout(Duration.ofSeconds(20)).block();

            ObjectMapper objectMapper = new ObjectMapper();

            SMAResponse response = objectMapper.readValue(rawResponse, SMAResponse.class);

            log.debug("SMA response parsed successfully: {}", response);

            if(response == null || response.technicalAnalysis() == null || response.technicalAnalysis().isEmpty()) {

                log.warn("No SMA200 data returned for {}", symbol);

                return 0;
            }

            String latestDate = response.technicalAnalysis().keySet().stream().max(String::compareTo).orElse(null);

            if(latestDate == null) {
                throw new Exception("SMA200 data not found for LatestDate");
            }

            SMAData smaData = response.technicalAnalysis().get(latestDate);

            double sma200 = Double.parseDouble(smaData.sma());

            log.info("Fetched SMA200={} for {}", sma200, symbol);
            return sma200;

        } catch(Exception ex) {
            throw new ApiException("Failed fetching market data from AlphaVantage", ex);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SMAResponse(@JsonProperty("Technical Analysis: SMA") Map<String, SMAData> technicalAnalysis) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SMAData(@JsonProperty("SMA") String sma) {
    }
}