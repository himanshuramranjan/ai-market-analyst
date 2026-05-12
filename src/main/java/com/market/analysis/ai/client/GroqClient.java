package com.market.analysis.ai.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroqClient {

    private final WebClient webClient;

    public String generateCommentary(String apiKey, String prompt) {

        Map<String, Object> request = Map.of("model", "llama-3.3-70b-versatile", "messages", new Object[]{Map.of("role", "user", "content", prompt)}, "temperature", 0.2, "max_tokens", 500);

        GroqResponse response = webClient.post().uri("https://api.groq.com/openai/v1/chat/completions").header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey).contentType(MediaType.APPLICATION_JSON).bodyValue(request).retrieve().bodyToMono(GroqResponse.class).block();

        if(response == null || response.choices() == null || response.choices().isEmpty()) {

            return "AI commentary unavailable.";
        }

        return response.choices().get(0).message().content();
    }
}

