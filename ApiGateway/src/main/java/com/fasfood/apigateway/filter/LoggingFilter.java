package com.fasfood.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.env.Environment;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

@Slf4j
@Component
public class LoggingFilter implements GlobalFilter {

    private static final String PUBLIC = "public";
    private static final String PROFILE = "fasfood-profile";

    private String currentProfile;

    public LoggingFilter(Environment environment) {
        List<String> profiles = Arrays.asList(environment.getActiveProfiles());
        Optional<String> profilePublic =
                profiles.stream().filter(item -> item.contains(PUBLIC)).findFirst();

        if (profilePublic.isPresent()) {
            this.currentProfile = PUBLIC;
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Set<URI> originalUris = exchange.getAttribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        if (originalUris != null) {
            URI originalUri = originalUris.iterator().next();

            Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);

            URI routeUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
            if (route == null) {
                log.info("Incoming request {} is routed to uri:{}", originalUri.toString(), routeUri);
            } else {
                log.info("Incoming request {} is routed to id: {}, uri:{}", originalUri.toString(), route.getId(), routeUri);
            }
        }

        if (Objects.nonNull(this.currentProfile)) {
            ServerHttpRequest mutateRequest =
                    exchange.getRequest().mutate().header(PROFILE, currentProfile).build();
            ServerWebExchange mutateServerWebExchange =
                    exchange.mutate().request(mutateRequest).build();

            return chain.filter(mutateServerWebExchange);
        }

        return chain.filter(exchange);
    }
}
