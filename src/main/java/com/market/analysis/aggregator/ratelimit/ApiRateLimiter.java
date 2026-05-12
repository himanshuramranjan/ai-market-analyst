package com.market.analysis.aggregator.ratelimit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiRateLimiter {

    /**
     * AlphaVantage free tier:
     * 5 requests/minute
     * Safer operating interval:
     * 1 request every 15 seconds
     */
    private static final long MINIMUM_INTERVAL_MS = 15_000;
    private long lastRequestTime = 0;

    public synchronized void acquire() {

        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - lastRequestTime;

        if(elapsed < MINIMUM_INTERVAL_MS) {

            long sleepTime = MINIMUM_INTERVAL_MS - elapsed;

            log.info("Rate limiting active. Sleeping {} ms", sleepTime);

            try {

                Thread.sleep(sleepTime);

            } catch(InterruptedException ex) {

                Thread.currentThread().interrupt();

                throw new IllegalStateException("Thread interrupted during API rate limiting", ex);
            }
        }

        lastRequestTime = System.currentTimeMillis();
    }
}