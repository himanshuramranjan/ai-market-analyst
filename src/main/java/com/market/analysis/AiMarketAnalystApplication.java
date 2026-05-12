package com.market.analysis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class AiMarketAnalystApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AiMarketAnalystApplication.class, args);

        Environment env = context.getEnvironment();

        log.info("========================================");
        log.info("Indian Stock Market Analysis System");
        log.info("========================================");
        log.info("Active Profiles: {}", Arrays.toString(env.getActiveProfiles()));
        log.info("Application started successfully");
        log.info("AI Commentary Engine: ENABLED");
        log.info("Telegram Notifications: ENABLED");
        log.info("========================================");
    }
}
