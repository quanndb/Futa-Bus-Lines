package com.fasfood.iamservice.application.service;

import com.fasfood.iamservice.infrastructure.persistence.readmodel.TokenLifeTimeProperties;
import com.fasfood.util.TimeConverter;
import com.fasfood.web.security.TokenCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Primary
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(TokenLifeTimeProperties.class)
public class TokenCacheServiceImpl implements TokenCacheService {

    private final RedisTemplate<String, String> redisTemplate;
    private final TokenLifeTimeProperties tokenLifeTimeProperties;

    @Override
    public void invalidToken(String token) {
        this.putValue(String.format("%s:%s", TokenCacheService.INVALID_TOKEN_CACHE, token), "invalid",
                        Duration.ofMillis(TimeConverter
                                .convertToMilliseconds(this.tokenLifeTimeProperties.getAccessTokenLifetime())));
    }

    @Override
    public void invalidRefreshToken(String refreshToken) {
        this.putValue(String.format("%s:%s", TokenCacheService.INVALID_REFRESH_TOKEN_CACHE, refreshToken), "invalid",
                        Duration.ofMillis(TimeConverter
                                .convertToMilliseconds(this.tokenLifeTimeProperties.getRefreshTokenLifetime())));
    }

    @Override
    public void invalidSid(String sid) {
        this.putValue(String.format("%s:%s", TokenCacheService.INVALID_SID_CACHE, sid), "invalid",
                        Duration.ofMillis(TimeConverter
                                .convertToMilliseconds(this.tokenLifeTimeProperties.getRefreshTokenLifetime())));
    }

    @Override
    public boolean isExisted(String cacheName, String token) {
        return this.isExisted(cacheName + ":" + token);
    }

    public void putValue(String key, String value, Duration lifeTime) {
        this.redisTemplate.opsForValue()
                .set(key, value, lifeTime);
    }

    public boolean isExisted(String key) {
        return this.redisTemplate.hasKey(key);
    }
}
