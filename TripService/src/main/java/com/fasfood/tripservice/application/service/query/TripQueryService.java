package com.fasfood.tripservice.application.service.query;

import com.fasfood.common.dto.PageDTO;
import com.fasfood.common.dto.request.PagingRequest;
import com.fasfood.common.dto.response.TripDetailsResponse;
import com.fasfood.common.dto.response.UserResponse;
import com.fasfood.common.query.QueryService;
import com.fasfood.tripservice.application.dto.request.TripFilterRequest;
import com.fasfood.tripservice.application.dto.request.TripPagingRequest;
import com.fasfood.tripservice.application.dto.response.TripDTO;
import com.fasfood.tripservice.application.dto.response.TripDetailsDTO;
import com.fasfood.tripservice.application.dto.response.TripResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TripQueryService extends QueryService<TripDTO, UUID, TripPagingRequest> {
    byte[] getTemplate();

    byte[] getTripDetailsTemplate();

    List<TripDetailsDTO> tripDetails(UUID id);

    List<TripResponse> findTrips(TripFilterRequest tripFilterRequest);

    TripDetailsResponse getTripDetails(UUID id, UUID departureId, UUID arrivalId, LocalDate departureDate);
}
