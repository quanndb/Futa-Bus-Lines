package com.fasfood.tripservice.infrastructure.persistence.repository.custom;

import com.fasfood.tripservice.application.dto.request.TripFilterRequest;

import java.util.List;
import java.util.UUID;

public interface CustomTripEntityRepository {
    List<UUID> findTrips(TripFilterRequest filter);
}
