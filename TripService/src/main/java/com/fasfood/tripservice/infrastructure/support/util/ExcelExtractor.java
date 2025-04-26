package com.fasfood.tripservice.infrastructure.support.util;

import com.fasfood.common.enums.BusTypeEnum;
import com.fasfood.commonexcel.CellConverter;
import com.fasfood.commonexcel.ExcelUtil;
import com.fasfood.tripservice.application.dto.request.RouteCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.request.TransitPointCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.request.TripCreator;
import com.fasfood.tripservice.application.dto.request.TripDetailsCreateOrUpdateRequest;
import com.fasfood.tripservice.infrastructure.support.enums.TransitType;
import com.fasfood.tripservice.infrastructure.support.enums.TripStatus;
import com.fasfood.tripservice.infrastructure.support.enums.TripTransitType;

public class ExcelExtractor {
    public static ExcelUtil.ExcelMapper<RouteCreateOrUpdateRequest> extractRoute() {
        return ExcelUtil.mapperFor(RouteCreateOrUpdateRequest.class)
                .addColumn("Departure Id", CellConverter.uuidConverter(),
                        "departureId", RouteCreateOrUpdateRequest::setDepartureId, RouteCreateOrUpdateRequest::getDepartureId)
                .addColumn("Destination Id", CellConverter.uuidConverter(),
                        "destinationId", RouteCreateOrUpdateRequest::setDestinationId, RouteCreateOrUpdateRequest::getDestinationId)
                .addColumn("Distance (km)", CellConverter.doubleConverter(),
                        "distance", RouteCreateOrUpdateRequest::setDistance, RouteCreateOrUpdateRequest::getDistance)
                .addColumn("Duration (min)", CellConverter.intConverter(),
                        "duration", RouteCreateOrUpdateRequest::setDuration, RouteCreateOrUpdateRequest::getDuration);
    }

    public static ExcelUtil.ExcelMapper<TransitPointCreateOrUpdateRequest> extractTransitPoint() {
        return ExcelUtil.mapperFor(TransitPointCreateOrUpdateRequest.class)
                .addColumn("Name", CellConverter.stringConverter(),
                        "name", TransitPointCreateOrUpdateRequest::setName, TransitPointCreateOrUpdateRequest::getName)
                .addColumn("Address", CellConverter.stringConverter(),
                        "address", TransitPointCreateOrUpdateRequest::setAddress, TransitPointCreateOrUpdateRequest::getAddress)
                .addColumn("Hotline (Optional)", CellConverter.stringConverter(),
                        "hotline", TransitPointCreateOrUpdateRequest::setHotline, TransitPointCreateOrUpdateRequest::getHotline)
                .addColumn("Type (STATION/OFFICE/TRANSPORT)", CellConverter.enumConverter(TransitType.class),
                        "type", TransitPointCreateOrUpdateRequest::setType, TransitPointCreateOrUpdateRequest::getType);
    }

    public static ExcelUtil.ExcelMapper<TripCreator> extractTrip() {
        return ExcelUtil.mapperFor(TripCreator.class)
                .addColumn("Code", CellConverter.stringConverter(),
                        "code", TripCreator::setCode, TripCreator::getCode)
                .addColumn("Name", CellConverter.stringConverter(),
                        "name", TripCreator::setName, TripCreator::getName)
                .addColumn("Address", CellConverter.stringConverter(),
                        "address", TripCreator::setAddress, TripCreator::getAddress)
                .addColumn("Hotline (Optional)", CellConverter.stringConverter(),
                        "hotline", TripCreator::setHotline, TripCreator::getHotline)
                .addColumn("Transit type (PLACE/STATION/OFFICE/TRANSPORT)", CellConverter.enumConverter(TransitType.class),
                        "transitType", TripCreator::setTransitType, TripCreator::getTransitType)
                .addColumn("Arrival time (HH:mm)", CellConverter.localTimeConverter(),
                        "arrivalTime", TripCreator::setArrivalTime, TripCreator::getArrivalTime)
                .addColumn("Trip transit type (PICKUP/DROP/BOTH)", CellConverter.enumConverter(TripTransitType.class),
                        "tripTransitType", TripCreator::setTripTransitType, TripCreator::getTripTransitType)
                ;
    }

    public static ExcelUtil.ExcelMapper<TripDetailsCreateOrUpdateRequest> extractTripDetails() {
        return ExcelUtil.mapperFor(TripDetailsCreateOrUpdateRequest.class)
                .addColumn("Trip code", CellConverter.stringConverter(),
                        "tripCode", TripDetailsCreateOrUpdateRequest::setTripCode, TripDetailsCreateOrUpdateRequest::getTripCode)
                .addColumn("From date (yy-mm-dd)", CellConverter.localDateConverter(),
                        "fromAt", TripDetailsCreateOrUpdateRequest::setFromDate, TripDetailsCreateOrUpdateRequest::getFromDate)
                .addColumn("To date (yy-mm-dd)", CellConverter.localDateConverter(),
                        "toAt", TripDetailsCreateOrUpdateRequest::setToDate, TripDetailsCreateOrUpdateRequest::getToDate)
                .addColumn("Type (SEAT/BED/LIMOUSINE)", CellConverter.enumConverter(BusTypeEnum.class),
                        "type", TripDetailsCreateOrUpdateRequest::setType, TripDetailsCreateOrUpdateRequest::getType)
                .addColumn("Price", CellConverter.longConverter(),
                        "price", TripDetailsCreateOrUpdateRequest::setPrice, TripDetailsCreateOrUpdateRequest::getPrice)
                .addColumn("Status (ACTIVE/INACTIVE)", CellConverter.enumConverter(TripStatus.class),
                        "status", TripDetailsCreateOrUpdateRequest::setStatus, TripDetailsCreateOrUpdateRequest::getStatus)
                ;
    }
}
