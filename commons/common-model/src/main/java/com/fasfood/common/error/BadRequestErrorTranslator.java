package com.fasfood.common.error;

import com.fasfood.common.validator.ExceptionTranslator;
import org.springframework.stereotype.Component;

@Component
public class BadRequestErrorTranslator implements ExceptionTranslator {
    @Override
    public String translate(String key) {
        return BadRequestError.valueOf(key).getMessage();
    }
}
