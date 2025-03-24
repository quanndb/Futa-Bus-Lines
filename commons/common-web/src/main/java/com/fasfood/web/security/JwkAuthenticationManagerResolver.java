package com.fasfood.web.security;

import com.fasfood.util.StrUtils;
import com.fasfood.web.config.JwtProperties;
import com.nimbusds.jwt.JWTParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class JwkAuthenticationManagerResolver implements AuthenticationManagerResolver<HttpServletRequest> {
    private final Map<String, String> issuers;
    private final Map<String, AuthenticationManager> authenticationManagers = new HashMap<>();
    private final BearerTokenResolver resolver = new DefaultBearerTokenResolver();

    public JwkAuthenticationManagerResolver(JwtProperties jwtProperties) {
        this.issuers = jwtProperties.getJwkSetUris();
    }

    public AuthenticationManager resolve(HttpServletRequest request) {
        return this.authenticationManagers.computeIfAbsent(this.toIssuerId(request), this::fromIssuer);
    }

    private String toIssuerId(HttpServletRequest request) {
        String token = this.resolver.resolve(request);
        try {
            if (StrUtils.isNotBlank((String) JWTParser.parse(token).getJWTClaimsSet().getClaim("email"))) {
                return "internal";
            } else {
                return StrUtils.isNotBlank((String) JWTParser.parse(token).getJWTClaimsSet().getClaim("client_id")) ? "sso" : "internal";
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private AuthenticationManager fromIssuer(String issuerId) {
        log.info("issuerId: {}", this.issuers);
        JwtAuthenticationProvider var10000 = Optional.ofNullable(this.issuers.get(issuerId))
                .map((issuer) -> NimbusJwtDecoder.withJwkSetUri(issuer).build())
                .map(JwtAuthenticationProvider::new).orElseThrow(() -> new IllegalArgumentException("unknown issuer"));
        Objects.requireNonNull(var10000);
        return var10000::authenticate;
    }
}
