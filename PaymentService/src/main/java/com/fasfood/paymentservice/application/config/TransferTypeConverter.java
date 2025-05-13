package com.fasfood.paymentservice.application.config;

import com.fasfood.common.enums.TransferType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TransferTypeConverter implements Converter<String, TransferType> {
    @Override
    public TransferType convert(String source) {
        return switch (source.toLowerCase()) {
            case "in" -> TransferType.IN;
            case "out" -> TransferType.OUT;
            default -> throw new IllegalArgumentException("Invalid transfer type: " + source);
        };
    }
}
