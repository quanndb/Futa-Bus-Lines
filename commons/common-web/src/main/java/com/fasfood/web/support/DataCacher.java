package com.fasfood.web.support;

import com.fasfood.common.error.InternalServerError;
import com.fasfood.common.exception.ResponseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@Component
public class DataCacher {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> void put(String key, T value) {
        this.put(key, value, null);
    }

    public <T> void put(String key, T value, Duration ttl) {
        try {
            String json = this.objectMapper.writeValueAsString(value);
            if (ttl != null) {
                this.redisTemplate.opsForValue().set(key, json, ttl);
            } else {
                this.redisTemplate.opsForValue().set(key, json);
            }
        } catch (JsonProcessingException e) {
            throw new ResponseException(InternalServerError.UNABLE_TO_PARSE_JSON);
        }
    }

    public <T> T get(String key, Class<T> type) {
        try {
            String json = this.redisTemplate.opsForValue().get(key);
            return json != null ? this.objectMapper.readValue(json, type) : null;
        } catch (JsonProcessingException e) {
            throw new ResponseException(InternalServerError.UNABLE_TO_PARSE_JSON);
        }
    }

    public void remove(String key) {
        this.redisTemplate.delete(key);
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(this.redisTemplate.hasKey(key));
    }
}
