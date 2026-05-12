package com.market.analysis;

import com.market.analysis.config.MarketAnalysisProperties;
import com.market.analysis.notification.TelegramMessageFormatter;
import com.market.analysis.notification.TelegramNotifier;
import com.market.analysis.orchestrator.AnalysisOrchestrator;
import com.market.analysis.orchestrator.AnalysisRequest;
import com.market.analysis.orchestrator.AnalysisResult;
import com.market.analysis.report.ConsoleReportGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevelopmentAnalysisRunner implements CommandLineRunner {

    private final AnalysisOrchestrator orchestrator;
    private final ConsoleReportGenerator reportGenerator;
    private final MarketAnalysisProperties properties;
    private final TelegramNotifier telegramNotifier;
    private final TelegramMessageFormatter telegramMessageFormatter;

    @Override
    public void run(String... args) {
        List<String> configuredStocks = properties.getStocks();
        /** NOTE : While running this app, we need to provide the keys in yaml file **/
        if(configuredStocks == null || configuredStocks.isEmpty()) {
            log.warn("No stocks configured for development run");
            return;
        }
        /** * IMPORTANT: * AlphaVantage free tier is limited. * Restrict development execution size. */
        List<String> stocksToAnalyze = configuredStocks.stream().limit(2).toList();

        log.info("========================================");
        log.info("Starting development analysis");
        log.info("Stocks: {}", stocksToAnalyze);
        log.info("========================================");
        AnalysisRequest request = new AnalysisRequest(stocksToAnalyze, true, true);
        List<AnalysisResult> results = orchestrator.analyzePortfolio(request);
        reportGenerator.printPortfolioReport(results);

        results.stream().filter(AnalysisResult::successful).forEach(result -> {
            try {
                String message = telegramMessageFormatter.format(result);
                telegramNotifier.sendMessage(message);
            } catch(Exception ex) {
                log.error("Failed sending Telegram notification for {}", result.symbol(), ex);
            }
        });

        log.info("========================================");
        log.info("Development analysis completed");
        log.info("========================================");

    }
}
