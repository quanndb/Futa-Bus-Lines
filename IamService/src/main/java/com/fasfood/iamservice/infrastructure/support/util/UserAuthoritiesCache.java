package com.fasfood.iamservice.infrastructure.support.util;

import com.fasfood.common.UserAuthority;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class UserAuthoritiesCache {
    private final RedisTemplate<String, String> redisTemplate;
    public static final String AUTHORITIES_KEY = "authorities";
    private final ObjectMapper objectMapper;

    public void putUserAuthorities(UserAuthority userAuthority) {
        try {
            String json = this.objectMapper.writeValueAsString(userAuthority);
            this.redisTemplate.opsForValue().set(String.join(":", AUTHORITIES_KEY, userAuthority.getUserId().toString()), json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing UserAuthority", e);
        }
    }

    public void removeUserAuthorities(UUID userId) {
        this.redisTemplate.delete(String.join(":", AUTHORITIES_KEY, userId.toString()));
    }

    public UserAuthority getUserAuthorities(UUID userId) {
        try {
            String json = this.redisTemplate.opsForValue().get(String.join(":", AUTHORITIES_KEY, userId.toString()));
            return json != null ? this.objectMapper.readValue(json, UserAuthority.class) : null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing UserAuthority", e);
        }
    }

    public boolean hasUserAuthorities(UUID userId) {
        return Boolean.TRUE.equals(this.redisTemplate.hasKey(String.join(":", AUTHORITIES_KEY, userId.toString())));
    }
}
