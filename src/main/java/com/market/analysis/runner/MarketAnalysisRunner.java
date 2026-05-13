package com.market.analysis.runner;

import com.market.analysis.config.MarketAnalysisProperties;
import com.market.analysis.notification.TelegramMessageFormatter;
import com.market.analysis.notification.TelegramNotifier;
import com.market.analysis.orchestrator.AnalysisOrchestrator;
import com.market.analysis.orchestrator.AnalysisRequest;
import com.market.analysis.orchestrator.AnalysisResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Profile("prod")
@RequiredArgsConstructor
public class MarketAnalysisRunner implements ApplicationRunner {

    private final AnalysisOrchestrator orchestrator;
    private final TelegramNotifier telegramNotifier;
    private final TelegramMessageFormatter formatter;
    private final MarketAnalysisProperties properties;

    @Override
    public void run(ApplicationArguments args) {

        log.info("Starting GitHub Actions market analysis");

        List<String> stocks = properties.getStocks();

        AnalysisRequest request = new AnalysisRequest(stocks, true, true);

        List<AnalysisResult> results = orchestrator.analyzePortfolio(request);

        results.stream().filter(AnalysisResult::successful).forEach(result -> {
            try {
                telegramNotifier.sendMessage(formatter.format(result));

            } catch(Exception ex) {
                log.error("Failed sending notification for {}", result.symbol(), ex);
            }
        });

        log.info("GitHub Actions market analysis completed");
    }
}