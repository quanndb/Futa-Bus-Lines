package com.fasfood.client.config;

import com.fasfood.common.dto.error.ErrorResponse;
import com.fasfood.common.exception.ForwardInnerAlertException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class CustomFeignErrorDecoder implements ErrorDecoder {
    private static final Logger log = LoggerFactory.getLogger(CustomFeignErrorDecoder.class);
    private final ErrorDecoder defaultErrorDecoder = new Default();
    private final ObjectMapper objectMapper;

    public CustomFeignErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        try (Reader reader = response.body().asReader(StandardCharsets.UTF_8)) {
            if (response.status() == HttpStatus.UNAUTHORIZED.value()) {
                log.error("Feign Client Unauthorized: {}", methodKey);
                return new ForwardInnerAlertException(new ErrorResponse<>(401, "Unauthorized", null, "Service Unauthorized"));
            }

            ErrorResponse<Void> errorResponse = objectMapper.readValue(reader, new TypeReference<>() {});
            log.error("Feign Client Error: {} - Status: {}", methodKey, response.status());
            return new ForwardInnerAlertException(errorResponse);

        } catch (IOException e) {
            log.error("Feign decode error");
            return this.defaultErrorDecoder.decode(methodKey, response);
        }
    }
}
