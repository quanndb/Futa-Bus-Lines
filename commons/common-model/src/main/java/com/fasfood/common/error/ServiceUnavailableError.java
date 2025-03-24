package com.fasfood.common.error;

import lombok.Getter;

@Getter
public enum ServiceUnavailableError implements ResponseError {
    SERVICE_UNAVAILABLE_ERROR(50300001, "Service unavailable"),
    ;

    private final Integer code;
    private final String message;

    private ServiceUnavailableError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getName() {
        return this.name();
    }

    public int getStatus() {
        return 503;
    }

}
