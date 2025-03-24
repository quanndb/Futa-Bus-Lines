package com.fasfood.web.config;

import com.fasfood.web.support.IamServiceLocator;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class JwtProperties {

    private final IamServiceLocator iamServiceLocator;

    public JwtProperties(IamServiceLocator iamServiceLocator) {
        this.iamServiceLocator = iamServiceLocator;
    }

    public Map<String, String> getJwkSetUris() {
        return Map.of(
                "sso", "http://localhost:8888/realms/FasFood/protocol/openid-connect/certs",
                "internal", iamServiceLocator.getIamServiceUrl() + "/api/certificate/.well-known/jwks.json"
        );
    }
}
