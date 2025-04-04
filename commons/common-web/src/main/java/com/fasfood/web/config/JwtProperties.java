package com.fasfood.web.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Getter
@Setter
@ConfigurationProperties(
        prefix = "spring.security.oauth2.resourceserver.jwt"
)
public class JwtProperties {
    private Map<String, String> jwkSetUris;
}
