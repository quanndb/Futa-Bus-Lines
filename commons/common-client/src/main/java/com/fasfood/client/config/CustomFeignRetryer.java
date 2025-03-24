package com.fasfood.client.config;

import feign.RetryableException;
import feign.Retryer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class CustomFeignRetryer implements Retryer {
    private static final Logger log = LoggerFactory.getLogger(CustomFeignRetryer.class);
    private final int maxAttempts;
    private final long backoff;
    private int attempt;

    public CustomFeignRetryer() {
        this(TimeUnit.SECONDS.toMillis(5L), 3);
    }

    public CustomFeignRetryer(long backoff, int maxAttempts) {
        this.backoff = backoff;
        this.maxAttempts = maxAttempts;
        this.attempt = 1;
    }

    @Override
    public void continueOrPropagate(RetryableException e) {
        if (this.attempt >= this.maxAttempts) {
            log.error("Retry failed after {} attempts for URL: {}. Exception: {}",
                    this.maxAttempts, e.request().url(), e.getMessage(), e);
            throw e;
        }

        log.warn("Retry attempt {} for URL: {} due to error: {}. Waiting {} ms before retrying...",
                this.attempt, e.request().url(), e.getMessage(), this.backoff);

        try {
            TimeUnit.MILLISECONDS.sleep(this.backoff);
        } catch (InterruptedException ie) {
            log.error("Retry sleep interrupted", ie);
            Thread.currentThread().interrupt();
        }

        this.attempt++;
    }

    @Override
    public Retryer clone() {
        return new CustomFeignRetryer(this.backoff, this.maxAttempts);
    }
}
