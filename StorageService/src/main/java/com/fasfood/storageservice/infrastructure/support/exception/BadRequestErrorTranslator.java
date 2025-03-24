package com.fasfood.storageservice.infrastructure.support.exception;

import com.fasfood.common.validator.ExceptionTranslator;
import org.springframework.stereotype.Component;

@Component
public class BadRequestErrorTranslator implements ExceptionTranslator {
    @Override
    public String translate(String key) {
        return BadRequestError.valueOf(key).getMessage();
    }
}
