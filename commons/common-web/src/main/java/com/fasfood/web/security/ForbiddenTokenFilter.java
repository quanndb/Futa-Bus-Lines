package com.fasfood.web.security;

import com.fasfood.common.dto.error.ErrorResponse;
import com.fasfood.common.error.AuthenticationError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWTParser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ForbiddenTokenFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(ForbiddenTokenFilter.class);
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final TokenCacheService tokenCacheService;
    private final ObjectMapper objectMapper;

    public ForbiddenTokenFilter(TokenCacheService tokenCacheService, ObjectMapper objectMapper) {
        this.tokenCacheService = tokenCacheService;
        this.objectMapper = objectMapper;
    }

    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null) {
            return true;
        } else if (authentication instanceof JwtAuthenticationToken) {
            return !authentication.isAuthenticated();
        } else {
            return authentication instanceof AnonymousAuthenticationToken;
        }
    }

    protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest, @NonNull HttpServletResponse httpServletResponse, @NonNull FilterChain filterChain) throws ServletException, IOException {
        this.logger.info("Forbidden token check");
        String token = this.resolveToken(httpServletRequest);
        String sid = this.extractSid(token);

        boolean isValidToken = !this.tokenCacheService.isInvalidToken(token);
        boolean isValidRefreshToken = !this.tokenCacheService.isInvalidRefreshToken(token);
        boolean isValidSid = StringUtils.hasText(sid) && !this.tokenCacheService.isInvalidSid(sid);

        if (isValidToken && isValidRefreshToken && isValidSid) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "InvalidJWT");
        httpServletResponse.getWriter().write(this.objectMapper.writeValueAsString(
                ErrorResponse.builder()
                        .error(AuthenticationError.UNAUTHORISED.getMessage())
                        .code(AuthenticationError.UNAUTHORISED.getCode())
                        .message("Unauthenticated")
                        .build()
        ));
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        return StringUtils.hasText(bearerToken)? bearerToken.substring(7) : null;
    }

    public String extractSid(String token) {
        try {
            return (String) JWTParser.parse(token).getJWTClaimsSet().getClaim("jti");
        } catch (Exception var3) {
            return null;
        }
    }
}