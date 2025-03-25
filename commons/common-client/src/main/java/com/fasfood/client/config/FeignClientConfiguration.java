package com.fasfood.client.config;

import com.fasfood.client.config.security.ClientAuthentication;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class FeignClientConfiguration {
    private static final Logger log = LoggerFactory.getLogger(FeignClientConfiguration.class);

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
                    if (!Objects.equals(headerName, "content-length")) {
                        String headerValue = request.getHeader(headerName);
                        requestTemplate.header(headerName, headerValue);
                    }
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
