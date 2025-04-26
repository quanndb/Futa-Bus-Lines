package com.fasfood.client.config;

import com.fasfood.client.config.security.ClientAuthentication;
import com.fasfood.common.dto.request.ClientRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({ClientRequest.class})
public class FeignClientConfiguration {
    private final ClientRequest clientRequest;
    private final ClientAuthentication clientAuthentication;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new FeignClientInterceptor(this.clientRequest, this.clientAuthentication);
    }

    @Bean
    feign.Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.BASIC;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomFeignErrorDecoder(new ObjectMapper());
    }

    @Bean
    public Retryer retryer() {
        return new CustomFeignRetryer();
    }
}
