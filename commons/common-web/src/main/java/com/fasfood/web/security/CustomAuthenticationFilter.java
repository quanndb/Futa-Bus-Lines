package com.fasfood.web.security;

import com.fasfood.common.UserAuthentication;
import com.fasfood.common.UserAuthenticationCreator;
import com.fasfood.common.UserAuthority;
import com.fasfood.common.enums.TokenType;
import com.fasfood.common.exception.ForwardInnerAlertException;
import com.fasfood.common.exception.ResponseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationFilter.class);
    private final AuthorityService authorityService;
    private final ObjectMapper objectMapper;

    @Value(value = "${app.security.enable-user}")
    private Boolean enableUser;

    public CustomAuthenticationFilter(AuthorityService authorityService) {
        this.authorityService = authorityService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.info("CustomAuthenticationFilter");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        JwtAuthenticationToken authentication =
                (JwtAuthenticationToken) securityContext.getAuthentication();
        Jwt token = authentication.getToken();

        String clientId = authentication.getToken().getClaim("client_id");

        if (clientId != null) {
            User principal = new User(clientId, "", Set.of());
            AbstractAuthenticationToken auth = new UserAuthentication(UserAuthenticationCreator.builder()
                    .tokenType(TokenType.ACCESS_TOKEN)
                    .isClient(true)
                    .isRoot(true)
                    .principal(principal)
                    .token(token.getTokenValue())
                    .credentials(token.getTokenValue())
                    .build());

            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        } else {
            if (!Boolean.TRUE.equals(this.enableUser)) {
                this.logger.error("Service available for client only");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 401
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Service available for client only\"}");
                return;
            }
            if (!request.getRequestURI().endsWith("/token")
                    && !request.getRequestURI().endsWith("/password")
                    && !TokenType.ACCESS_TOKEN.toString().equals(token.getClaim("type"))) {
                this.logger.error("Must have access token");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Must have access token\"}");
                return;
            }
            Boolean isRoot = Boolean.FALSE;

            UserAuthority optionalUserAuthority;
            try {
                optionalUserAuthority = this.enrichAuthority(token);
            } catch (Exception e) {
                if (e instanceof ResponseException responseException) {
                    this.logger.error(responseException.getMessage());
                    response.setStatus(responseException.getError().getStatus());
                    response.setContentType("application/json");
                    this.objectMapper.writeValue(response.getWriter(), responseException.getError());
                    return;
                }
                if (e instanceof ForwardInnerAlertException forwardInnerAlertException) {
                    this.logger.error(forwardInnerAlertException.getMessage());
                    response.setStatus(forwardInnerAlertException.getResponse().getCode());
                    response.setContentType("application/json");
                    this.objectMapper.writeValue(response.getWriter(), forwardInnerAlertException.getResponse());
                    return;
                }
                this.logger.error("Service is unavailable");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Service is unavailable\"}");
                return;
            }

            Set<SimpleGrantedAuthority> grantedPermissions = new HashSet<>();
            if (optionalUserAuthority != null) {
                isRoot = optionalUserAuthority.getIsRoot();
                grantedPermissions = optionalUserAuthority.getGrantedPermissions().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet());
            }

            String username;
            if (StringUtils.hasText(token.getClaimAsString("client_id"))) {
                username = token.getClaimAsString("client_id");
            } else {
                username = token.getClaimAsString("email");
            }

            User principal = new User(username, "", grantedPermissions);
            AbstractAuthenticationToken auth = new UserAuthentication(UserAuthenticationCreator.builder()
                    .principal(principal)
                    .userId(UUID.fromString(token.getSubject()))
                    .email(token.getClaimAsString("email"))
                    .isRoot(isRoot)
                    .isClient(false)
                    .credentials(token.getTokenValue())
                    .authorities(grantedPermissions)
                    .token(token.getTokenValue())
                    .tokenType(TokenType.valueOf(token.getClaimAsString("type")))
                    .build());

            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        return !(authentication instanceof JwtAuthenticationToken);
    }

    private UserAuthority enrichAuthority(Jwt token) {
        // Call lấy UserAuthority từ IAM dựa vào AuthorityService lưu ý với service khác IAM thì impl sẽ là RemoteAuthorityServiceImpl,
        // IAM thì sẽ dùng AuthorityServiceImpl(@Primary)
        return this.authorityService.getUserAuthority(UUID.fromString(token.getClaimAsString("sub")));
    }
}
