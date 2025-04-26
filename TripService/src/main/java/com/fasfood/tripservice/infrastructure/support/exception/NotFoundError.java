package com.fasfood.tripservice.infrastructure.support.exception;

import com.fasfood.common.error.ResponseError;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NotFoundError implements ResponseError {
    PLACE_NOT_FOUND(4000001, "Place not found: ${0}"),
    BUS_NOT_FOUND(4000002, "Bus not found: ${0}"),
    TRIP_DETAILS_NOT_FOUND(4000002, "Trip details not found: ${0}"),
    USER_NOT_FOUND(4000003, "User not found: ${0}"),
    BUS_TYPE_NOT_FOUND(4000004, "Bus type not found: ${0}"),
    TRIP_NOT_FOUND(4000005, "Trip not found: ${0}"),
    TRIP_TRANSIT_NOT_FOUND(4000006, "Trip transit not found: ${0}"),
    TRANSIT_POINT_NOT_FOUND(4000007, "Transit point not found: ${0}"),
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
