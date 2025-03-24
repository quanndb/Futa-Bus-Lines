package com.fasfood.iamservice.infrastructure.persistence.readmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "app.security.token")
public class TokenLifeTimeProperties {
    private String accessTokenLifetime;
    private String refreshTokenLifetime;
}
