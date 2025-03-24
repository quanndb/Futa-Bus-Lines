package com.fasfood.web.security.impl;

import com.fasfood.web.security.TokenCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoteTokenCacheServiceImpl implements TokenCacheService {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void invalidToken(String token) {
    }

    @Override
    public void invalidSid(String sid) {
    }

    @Override
    public void invalidRefreshToken(String refreshToken) {
    }

    @Override
    public boolean isExisted(String cacheName, String token) {
        return this.redisTemplate.hasKey(String.join(":", cacheName, token));
    }
}
