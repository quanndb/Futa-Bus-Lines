package com.fasfood.web.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonMapper {
    private final ObjectMapper objectMapper;

    public String toJson(Object obj) throws JsonProcessingException {
        return this.objectMapper.writeValueAsString(obj);
    }

    public <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return this.objectMapper.readValue(json, clazz);
    }
}
