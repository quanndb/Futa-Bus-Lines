package com.fasfood.tripservice.infrastructure.support.util;

import com.fasfood.commonexcel.CellConverter;
import com.fasfood.commonexcel.ExcelUtil;
import com.fasfood.tripservice.application.dto.request.PlaceCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.request.RouteCreateOrUpdateRequest;

public class ExcelExtractor {
    public static ExcelUtil.ExcelMapper<PlaceCreateOrUpdateRequest> extractPlace() {
        return ExcelUtil.mapperFor(PlaceCreateOrUpdateRequest.class)
                // Add columns with their converters and property setters/getters
                .addColumn("Name", String.class, CellConverter.stringConverter(),
                        "name", PlaceCreateOrUpdateRequest::setName, PlaceCreateOrUpdateRequest::getName)
                .addColumn("Address", String.class, CellConverter.stringConverter(),
                        "address", PlaceCreateOrUpdateRequest::setAddress, PlaceCreateOrUpdateRequest::getAddress)
                .addColumn("Code", String.class, CellConverter.stringConverter(),
                        "code", PlaceCreateOrUpdateRequest::setCode, PlaceCreateOrUpdateRequest::getCode);
    }

    public static ExcelUtil.ExcelMapper<RouteCreateOrUpdateRequest> extractRoute() {
        return ExcelUtil.mapperFor(RouteCreateOrUpdateRequest.class)
                .addColumn("Departure Code", String.class, CellConverter.stringConverter(),
                        "departureCode", RouteCreateOrUpdateRequest::setDepartureCode, RouteCreateOrUpdateRequest::getDepartureCode)
                .addColumn("Destination Code", String.class, CellConverter.stringConverter(),
                        "destinationCode", RouteCreateOrUpdateRequest::setDestinationCode, RouteCreateOrUpdateRequest::getDestinationCode)
                .addColumn("Distance (km)", Double.class, CellConverter.doubleConverter(),
                        "distance", RouteCreateOrUpdateRequest::setDistance, RouteCreateOrUpdateRequest::getDistance)
                .addColumn("Duration (min)", Integer.class, CellConverter.intConverter(),
                        "duration", RouteCreateOrUpdateRequest::setDuration, RouteCreateOrUpdateRequest::getDuration);
    }
}
