package com.market.analysis.aggregator.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlphaVantageResponse {
    private final Map <String, DailyData> timeSeries = new HashMap <>();
    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
        if (key.contains("Time Series")) {
            Map<String, Map <String, String>> data = (Map<String, Map<String, String>>) value;
            data.forEach((date, values) -> {
                    DailyData dailyData = new DailyData(
                            values.get("1. open"),
                            values.get("2. high"),
                            values.get("3. low"),
                            values.get("4. close"),
                            values.get("5. volume")
                    );
            timeSeries.put(date, dailyData);
            });
        }
    }

    public record DailyData(String open, String high, String low, String close, String volume) {}
}