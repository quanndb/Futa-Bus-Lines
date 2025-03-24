package com.fasfood.iamservice.infrastructure.persistence.readmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "app.security.keystore")
public class AuthenticationProperties {
    private String name;
    private String password;
    private String alias;
}
