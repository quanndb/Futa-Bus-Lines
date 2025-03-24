package com.fasfood.iamservice.infrastructure.support.exception;

import com.fasfood.common.error.ResponseError;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NotFoundError implements ResponseError {
    ACCOUNT_NOTFOUND(4040001, "Account not found"),
    ROLE_NOTFOUND(4040002, "Role not found: {0}"),
    PERMISSION_NOTFOUND(4040003, "Permission not found: {0}"),
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

