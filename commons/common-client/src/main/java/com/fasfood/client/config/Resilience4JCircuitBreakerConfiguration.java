package com.fasfood.client.config;

import com.fasfood.common.exception.ForwardInnerAlertException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import lombok.NoArgsConstructor;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@NoArgsConstructor
public class Resilience4JCircuitBreakerConfiguration {
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(100)
                .minimumNumberOfCalls(100)
                .failureRateThreshold(50.0F)
                .permittedNumberOfCallsInHalfOpenState(3)
                .waitDurationInOpenState(Duration.ofSeconds(30L))
                .ignoreExceptions(new Class[]{ForwardInnerAlertException.class}).build();
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(120L)).build();
        return (factory) -> factory
                .configureDefault((id) -> (new Resilience4JConfigBuilder(id))
                        .circuitBreakerConfig(circuitBreakerConfig).timeLimiterConfig(timeLimiterConfig).build());
    }
}