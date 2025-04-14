package com.fasfood.tripservice.domain.repository;

import com.fasfood.tripservice.domain.BusType;
import com.fasfood.web.support.DomainRepository;

import java.util.List;
import java.util.UUID;

public interface BusTypeRepository extends DomainRepository<BusType, UUID> {
    List<BusType> findAll();
}
