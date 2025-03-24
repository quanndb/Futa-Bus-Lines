package com.fasfood.storageservice.infrastructure.support.exception;

import com.fasfood.common.error.ResponseError;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BadRequestError implements ResponseError {
    UN_SUPPORT_FILE_TYPE(4000001, "Un support File Type: {0}"),
    INVALID_FILE_NAME(4000002, "Invalid File Name: {0}"),
    FILE_NOT_FOUND(4000003, "File Not Found: {0}"),
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
        return 400;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
