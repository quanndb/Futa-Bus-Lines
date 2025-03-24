package com.fasfood.common.exception;

import com.fasfood.common.error.ResponseError;
import lombok.Getter;

import java.text.MessageFormat;
import java.util.Objects;

@Getter
public class ResponseException extends RuntimeException {
    protected final ResponseError error;
    protected final Object[] params;

    public ResponseException(ResponseError error) {
        this(error.getMessage(), (Throwable) null, (ResponseError) error);
    }

    public ResponseException(String message, Throwable cause, ResponseError error) {
        this(message, cause, error, (Object) null);
    }

    public ResponseException(String message, Throwable cause, ResponseError error, Object... params) {
        super(Objects.nonNull(message) ? MessageFormat.format(message, params) : error.getMessage(), cause);
        this.error = error;
        this.params = params == null ? new Object[0] : params;
    }

    public ResponseException(ResponseError error, Object... params) {
        this(error.getMessage(), (Throwable) null, error, params);
    }

    public ResponseException(String message, ResponseError error) {
        this(message, (Throwable) null, (ResponseError) error);
    }

    public ResponseException(String message, ResponseError error, Object... params) {
        this(message, (Throwable) null, error, params);
    }
}
