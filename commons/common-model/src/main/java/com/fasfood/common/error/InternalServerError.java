package com.fasfood.common.error;

import lombok.Getter;

@Getter
public enum InternalServerError implements ResponseError {
    INTERNAL_SERVER_ERROR(50000001, "There are somethings wrong : {0}"),
    DATA_ACCESS_EXCEPTION(50000002, "Data access exception"),
    UNABLE_GET_IP(50000003,"Không thể lấy địa chỉ IP người dùng"),
    UNABLE_TO_PARSE_JSON(50000004, "Unable to parse JSON"),
    INVALID_AUTH_CODE(50000005, "Invalid auth code"),
    INVALID_TOKEN(50000006, "Invalid token"),
    UNABLE_TO_SEND_EMAIL(50000007, "Unable to send email"),
    UNABLE_TO_GET_CLIENT_TOKEN(50000008, "Unable to get client token"),
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
