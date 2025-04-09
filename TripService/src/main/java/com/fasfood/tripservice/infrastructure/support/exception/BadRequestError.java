package com.fasfood.tripservice.infrastructure.support.exception;

import com.fasfood.common.error.ResponseError;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BadRequestError implements ResponseError {
    NAME_REQUIRED(4000001,"Place name is required"),
    ADDRESS_REQUIRED(4000002,"Address is required"),
    CODE_REQUIRED(4000003,"Code is required"),
    PLACE_REQUIRED(4000004,"Place is required"),
    INVALID_FORMAT(4000005,"Invalid format: ${0}"),
    EXISTED_CODE(4000006,"Place already existed: ${0}"),
    NOT_EXISTED_CODE(4000007,"Place not existed: ${0}"),
    DEPARTURE_CODE_REQUIRED(4000008,"Departure code is required"),
    DESTINATION_CODE_REQUIRED(4000009,"Destination code is required"),
    DISTANCE_REQUIRED(4000010,"Distance is required"),
    DURATION_REQUIRED(4000011,"Duration is required"),
    EXISTED_ROUTE(4000012,"Route already existed: ${0}"),
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
