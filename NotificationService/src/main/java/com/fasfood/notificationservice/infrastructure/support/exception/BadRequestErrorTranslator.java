package com.fasfood.notificationservice.infrastructure.support.exception;

import com.fasfood.common.validator.ExceptionTranslator;
import org.springframework.stereotype.Component;

@Component
public class BadRequestErrorTranslator implements ExceptionTranslator {
    @Override
    public String translate(String key) {
        try {
            return BadRequestError.valueOf(key).getMessage();
        } catch (IllegalArgumentException e) {
            return "Unknown error key: " + key;
        }
    }
}
