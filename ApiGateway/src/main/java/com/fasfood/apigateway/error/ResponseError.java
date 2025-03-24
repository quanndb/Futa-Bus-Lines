package com.fasfood.apigateway.error;

import java.io.Serializable;

public interface ResponseError extends Serializable {

    String getName();

    String getMessage();

    int getStatus();

    default Integer getCode() {
        return 0;
    }
}
