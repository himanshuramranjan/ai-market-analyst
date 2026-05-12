package com.market.analysis.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "market-analysis")
public class MarketAnalysisProperties {

    private List<String> stocks = new ArrayList<>();

    private Scheduler scheduler = new Scheduler();

    private Api api = new Api();

    private Ai ai = new Ai();

    private Telegram telegram = new Telegram();

    @Data
    public static class Telegram {

        private boolean enabled;

        private String botToken;

        private String chatId;
    }

    @Data
    public static class Ai {

        private Groq groq = new Groq();
    }

    @Data
    public static class Groq {

        private String apiKey;
    }

    @Getter
    @Setter
    public static class Api {
        private AlphaVantage alphaVantage = new AlphaVantage();

        @Getter
        @Setter
        public static class AlphaVantage {
            private String apiKey;
            private String baseUrl;
        }
    }

    @Getter
    @Setter
    public static class Scheduler {

        private String cron = "0 0 9 * * MON-FRI";

        private String closeCron = "0 30 15 * * MON-FRI";
    }
}