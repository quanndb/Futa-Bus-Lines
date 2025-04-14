package com.fasfood.tripservice.infrastructure.support.exception;

import com.fasfood.common.error.ResponseError;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BadRequestError implements ResponseError {
    NAME_REQUIRED(4000001, "Place name is required"),
    ADDRESS_REQUIRED(4000002, "Address is required"),
    CODE_REQUIRED(4000003, "Code is required"),
    PLACE_REQUIRED(4000004, "Place is required"),
    INVALID_FORMAT(4000005, "Invalid format: ${0}"),
    EXISTED_CODE(4000006, "Place already existed: ${0}"),
    NOT_EXISTED_CODE(4000007, "Place not existed: ${0}"),
    DEPARTURE_CODE_REQUIRED(4000008, "Departure code is required"),
    DESTINATION_CODE_REQUIRED(4000009, "Destination code is required"),
    DISTANCE_REQUIRED(4000010, "Distance is required"),
    DURATION_REQUIRED(4000011, "Duration is required"),
    EXISTED_ROUTE(4000012, "Route already existed: ${0}"),
    LICENSE_PLATE_REQUIRED(4000013, "License plate is required"),
    BUS_TYPE_REQUIRED(4000014, "Bus type is required"),
    EXISTED_LICENSE_PLATE(4000015, "License plate already existed: ${0}"),
    EXISTED_TRANSIT_NAME(4000016, "Transit name already existed: ${0}"),
    TRANSIT_NAME_REQUIRED(4000017, "Transit name is required"),
    TRANSIT_ADDRESS_REQUIRED(4000018, "Transit address is required"),
    TRANSIT_PLACE_CODE_REQUIRED(4000019, "Transit place code is required"),
    TRANSIT_TYPE_REQUIRED(4000020, "Transit type is required"),
    EXISTED_TRIP_CODE(4000021, "Trip code already existed: ${0}"),
    NOT_EXISTED_TRANSIT_POINT(4000022, "Transit point not existed: ${0}"),
    SCHEDULE_ALREADY_EXISTED(4000023, "Schedule already existed: ${0} - ${1}"),
    DEPARTURE_REQUIRED(4000024, "Departure is required"),
    DESTINATION_REQUIRED(4000025, "Destination is required"),
    DEPARTURE_DATE_REQUIRED(4000026, "Departure date is required"),
    EXISTED_BUS_TYPE(4000027, "Bus type already existed: ${0}"),
    INVALID_BUS_TYPE(4000028, "Invalid bus type: ${0}, required: ${1}"),
    FROM_MUST_BEFORE_TO(4000029, "From must before to: ${0}"),
    INVALID_DATE(4000030, "Invalid date: ${0}"),
    NOT_EXISTED_TRIP_CODE(4000031, "Trip code not existed: ${0}"),
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
