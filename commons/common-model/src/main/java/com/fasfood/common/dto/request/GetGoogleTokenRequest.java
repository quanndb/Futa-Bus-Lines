package com.fasfood.common.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ConfigurationProperties(prefix = "app.security.provider.google")
public class GetGoogleTokenRequest {
    private String grantType = "authorization_code";
    private String code;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
