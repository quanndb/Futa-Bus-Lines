package com.fasfood.apigateway.exception;

import com.fasfood.apigateway.error.ResponseError;
import lombok.Getter;
import lombok.Setter;

import java.text.MessageFormat;

@Getter
@Setter
public class ResponseException extends RuntimeException {

    private com.fasfood.apigateway.error.ResponseError error;
    private Object[] params;

    public ResponseException(ResponseError error) {
        this(error.getMessage(), null, error);
    }

    public ResponseException(String message, Throwable cause, ResponseError error) {
        this(message, cause, error, (Object) null);
    }

    public ResponseException(
            String message, Throwable cause, ResponseError error, Object... params) {
        super(MessageFormat.format(message, params), cause);
        this.error = error;
        this.params = params == null ? new Object[0] : params;
    }

    public ResponseException(String message, ResponseError error) {
        this(message, null, error);
    }

    public ResponseException(String message, ResponseError error, Object... params) {
        this(message, null, error, params);
    }
}
