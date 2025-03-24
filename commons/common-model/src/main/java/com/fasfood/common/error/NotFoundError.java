package com.fasfood.common.error;

import lombok.Getter;

@Getter
public enum NotFoundError implements ResponseError {
    NOT_FOUND(40400001, "Not found"),
    PAGE_NOT_FOUND(40400002, "Page not found: {0}"),
    USER_NOT_FOUND(40400003, "User not found: {0}"),
    DATA_PERMISSION_NOT_FOUND(40400004, "Data permission not found"),
    POLICY_RESOURCE_SCOPE_NOT_FOUND(40400005, "Policy resource scope not found");

    private final Integer code;
    private final String message;

    private NotFoundError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getName() {
        return this.name();
    }

    public int getStatus() {
        return 404;
    }

}
