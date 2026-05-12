package com.market.analysis.notification;

import com.market.analysis.config.MarketAnalysisProperties;
import com.market.analysis.exception.NotificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("prod")
public class TelegramNotifier {

    private final WebClient webClient;

    private final MarketAnalysisProperties properties;

    public void sendMessage(String message) {

        var telegram = properties.getTelegram();

        if(!telegram.isEnabled()) {

            log.info("Telegram notifications disabled");

            return;
        }

        try {

            String url = "https://api.telegram.org/bot" + telegram.getBotToken() + "/sendMessage";

            Map<String, Object> request = Map.of("chat_id", telegram.getChatId(),

                    "text", message,

                    "parse_mode", "Markdown");

            webClient.post().uri(url).contentType(MediaType.APPLICATION_JSON).bodyValue(request).retrieve().bodyToMono(String.class).block();

            log.info("Telegram notification sent successfully");

        } catch(Exception ex) {

            throw new NotificationException("Failed sending Telegram notification " + ex);
        }
    }
}
