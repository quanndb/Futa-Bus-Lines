package com.fasfood.client.config;

import com.fasfood.client.config.security.ClientAuthentication;
import com.fasfood.common.dto.request.ClientRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.spring.SpringFormEncoder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class FeignClientConfiguration {
    private static final Logger log = LoggerFactory.getLogger(FeignClientConfiguration.class);

    @Value("${app.security.client.id}")
    private String clientId;
    @Value("${app.security.client.client-secret}")
    private String clientSecret;
    private final ClientAuthentication clientAuthentication;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            log.debug("Applying Feign request interceptor");

            var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                var request = attributes.getRequest();
                var headers = request.getHeaderNames();
                while (headers.hasMoreElements()) {
                    String headerName = headers.nextElement();
                    if(!Objects.equals(headerName, "content-length")) {
                        String headerValue = request.getHeader(headerName);
                        requestTemplate.header(headerName, headerValue);
                    }
                }
            }

            if (!requestTemplate.headers().containsKey("Authorization")) {
                var tokenResponse = this.clientAuthentication
                        .getClientToken(new ClientRequest(this.clientId, this.clientSecret, "client_credentials"));
                if (tokenResponse != null) {
                    requestTemplate.header("Authorization", "Bearer " + tokenResponse.getAccessToken());
                }
            }
        };
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
