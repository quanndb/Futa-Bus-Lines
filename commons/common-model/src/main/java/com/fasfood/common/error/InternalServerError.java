package com.fasfood.common.error;

import lombok.Getter;

@Getter
public enum InternalServerError implements ResponseError {
    INTERNAL_SERVER_ERROR(50000001, "There are somethings wrong : {0}"),
    DATA_ACCESS_EXCEPTION(50000002, "Data access exception"),
    UNABLE_GET_MAC(50000003, "Unable to get mac"),
    ;

    private final Integer code;
    private final String message;

    InternalServerError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getName() {
        return this.name();
    }

    public int getStatus() {
        return 500;
    }

}
