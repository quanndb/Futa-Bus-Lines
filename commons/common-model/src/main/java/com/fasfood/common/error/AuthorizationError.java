package com.fasfood.common.error;

import lombok.Getter;

@Getter
public enum AuthorizationError implements ResponseError {
    ACCESS_DENIED(40300001, "Access Denied"),
    NOT_SUPPORTED_AUTHENTICATION(40300002, "Your authentication has not been supported yet");

    private final Integer code;
    private final String message;

    AuthorizationError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getName() {
        return this.name();
    }

    public int getStatus() {
        return 403;
    }

}
