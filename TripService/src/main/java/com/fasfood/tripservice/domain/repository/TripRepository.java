package com.fasfood.tripservice.domain.repository;

import com.fasfood.tripservice.domain.Trip;
import com.fasfood.web.support.DomainRepository;

import java.util.UUID;

public interface TripRepository extends DomainRepository<Trip, UUID> {
}
