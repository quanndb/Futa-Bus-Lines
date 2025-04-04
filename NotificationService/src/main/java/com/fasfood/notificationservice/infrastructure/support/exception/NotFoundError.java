package com.fasfood.notificationservice.infrastructure.support.exception;

import com.fasfood.common.error.ResponseError;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NotFoundError implements ResponseError {
    TEMPLATE_NOTFOUND(4040001, "Template not found: ${0}"),
    ;

    private final Integer code;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public int getStatus() {
        return 404;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}

