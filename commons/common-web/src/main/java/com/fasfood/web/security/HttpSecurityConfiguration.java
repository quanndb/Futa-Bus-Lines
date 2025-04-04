package com.fasfood.web.security;

import com.fasfood.web.config.JwtProperties;
import com.fasfood.web.config.SpringSecurityAuditorAware;
import com.fasfood.web.config.StatsTracingFilter;
import com.fasfood.web.support.CustomAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@EnableFeignClients(
        basePackages = {"com.fasfood.client"}
)
@EnableMethodSecurity(
        securedEnabled = true
)
@RequiredArgsConstructor
@Component
public class HttpSecurityConfiguration {
    private static final Logger log = LoggerFactory.getLogger(HttpSecurityConfiguration.class);
    private final CorsFilter corsFilter;
    private final CustomAuthenticationFilter customAuthenticationFilter;
    private final ForbiddenTokenFilter forbiddenTokenFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final RegexPermissionEvaluator customPermissionEvaluator;
    private final StatsTracingFilter statsTracingFilter;
    private final JwtProperties jwtProperties;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(HttpMethod.OPTIONS, new String[]{"/**"})
                .requestMatchers(new String[]{"/js/*.js"}
                ).requestMatchers(new String[]{"/js/*.html"})
                .requestMatchers(new String[]{"/content/**"})
                .requestMatchers(HttpMethod.GET, new String[]{"/api/public/**"})
                .requestMatchers(HttpMethod.POST, new String[]{"/api/public/**"})
                .requestMatchers(new String[]{"/swagger-ui/index.html"});
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(this.corsFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/health",
                                "/api/public/**",
                                "/api/certificate/.well-known/jwks.json",
                                "/api/*/auth/login",
                                "/api/*/auth/provider/**",
                                "/api/*/accounts/register",
                                "/api/*/accounts/forgot-password",
                                "/swagger-*/**",
                                "/v2/api-docs",
                                "/v3/api-docs/**",
                                "/error",
                                "/actuator/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/*/files/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                );
        http.oauth2ResourceServer((oauth2) -> oauth2
                .authenticationManagerResolver(this.jwkResolver(this.jwtProperties))).exceptionHandling((exHandling) -> exHandling.authenticationEntryPoint(this.customAuthenticationEntryPoint));

        http.addFilterBefore(this.statsTracingFilter, BearerTokenAuthenticationFilter.class);
        http.addFilterAfter(this.forbiddenTokenFilter, BearerTokenAuthenticationFilter.class);
        http.addFilterAfter(this.customAuthenticationFilter, BearerTokenAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public MethodSecurityExpressionHandler expressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(this.customPermissionEvaluator);
        return expressionHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuditorAware<String> springSecurityAuditorAware() {
        return new SpringSecurityAuditorAware();
    }

    public AuthenticationManagerResolver<HttpServletRequest> jwkResolver(JwtProperties jwtProperties) {
        return new JwkAuthenticationManagerResolver(jwtProperties);
    }
}
