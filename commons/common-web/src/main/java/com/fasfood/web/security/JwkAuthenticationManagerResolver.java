package com.fasfood.web.security;

import com.fasfood.util.StrUtils;
import com.fasfood.web.config.JwtProperties;
import com.fasfood.web.support.CustomBearTokenResolver;
import com.nimbusds.jwt.JWTParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@EnableConfigurationProperties(JwtProperties.class)
public class JwkAuthenticationManagerResolver implements AuthenticationManagerResolver<HttpServletRequest> {
    private static Map<String, String> issuers = new HashMap<>();
    private final Map<String, AuthenticationManager> authenticationManagers = new HashMap<>();
    private final BearerTokenResolver resolver = new CustomBearTokenResolver();

    public JwkAuthenticationManagerResolver(JwtProperties jwtProperties) {
        issuers = jwtProperties.getJwkSetUris();
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
        log.info("issuerId: {}", issuers);
        JwtAuthenticationProvider var10000 = Optional.ofNullable(issuers.get(issuerId))
                .map((issuer) -> NimbusJwtDecoder.withJwkSetUri(issuer).build())
                .map(JwtAuthenticationProvider::new).orElseThrow(() -> new IllegalArgumentException("unknown issuer"));
        Objects.requireNonNull(var10000);
        return var10000::authenticate;
    }

    public static boolean isValidatedToken(String token) {
        try {
            var claims = JWTParser.parse(token).getJWTClaimsSet();
            String issuerId;
            if (StrUtils.isNotBlank((String) claims.getClaim("email"))) {
                issuerId = "internal";
            } else if (StrUtils.isNotBlank((String) claims.getClaim("client_id"))) {
                issuerId = "sso";
            } else {
                issuerId = "internal"; // fallback
            }
            String jwkSetUri = issuers.get(issuerId);
            if (jwkSetUri == null) {
                log.error("Unknown issuerId: {}", issuerId);
                return false;
            }
            NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
            jwtDecoder.decode(token);
            return true;
        } catch (Exception ex) {
            log.error("JWT validation failed: {}", ex.getMessage());
            return false;
        }
    }
}
