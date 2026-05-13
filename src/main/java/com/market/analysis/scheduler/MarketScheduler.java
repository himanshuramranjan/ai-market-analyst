package com.market.analysis.scheduler;

import com.market.analysis.config.MarketAnalysisProperties;
import com.market.analysis.notification.TelegramMessageFormatter;
import com.market.analysis.notification.TelegramNotifier;
import com.market.analysis.orchestrator.AnalysisOrchestrator;
import com.market.analysis.orchestrator.AnalysisRequest;
import com.market.analysis.orchestrator.AnalysisResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Profile("dev")
@Slf4j
@Service
@RequiredArgsConstructor
public class MarketScheduler {

    private final AnalysisOrchestrator orchestrator;

    private final TelegramNotifier telegramNotifier;

    private final TelegramMessageFormatter formatter;

    private final MarketAnalysisProperties properties;

    /**
     * Market Open Analysis
     */
    @Scheduled(cron = "${market-analysis.scheduler.cron:0 15 18 * * MON-FRI}", zone = "Asia/Kolkata")
    public void runDailyAnalysis() {

        log.info("Market open analysis triggered");

        runAnalysis("Market Open");
    }

    private void runAnalysis(String analysisType) {

        try {

            List<String> configuredStocks = properties.getStocks();

            if(configuredStocks == null || configuredStocks.isEmpty()) {

                log.warn("No configured stocks found for {} analysis", analysisType);

                return;
            }

            log.info("Starting {} analysis for {} stocks", analysisType, configuredStocks.size());

            AnalysisRequest request = new AnalysisRequest(configuredStocks, true, true);

            List<AnalysisResult> results = orchestrator.analyzePortfolio(request);

            /**
             * Notify only meaningful setups
             */
            results.stream()

                    .filter(AnalysisResult::successful)

                    .forEach(result -> {
                        try {
                            String message = formatter.format(result);
                            telegramNotifier.sendMessage(message);

                        } catch(Exception ex) {
                            log.error("Failed sending Telegram notification for {}", result.symbol(), ex);
                        }
                    });

            log.info("{} analysis completed successfully", analysisType);

        } catch(Exception e) {

            log.error("Scheduled analysis failed for {}: {}", analysisType, e.getMessage(), e);
        }
    }
}