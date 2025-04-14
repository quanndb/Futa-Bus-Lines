package com.fasfood.tripservice.infrastructure.domainrepository;

import com.fasfood.common.error.NotFoundError;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.tripservice.domain.Trip;
import com.fasfood.tripservice.domain.TripDetails;
import com.fasfood.tripservice.domain.TripTransit;
import com.fasfood.tripservice.domain.repository.TripRepository;
import com.fasfood.tripservice.infrastructure.persistence.entity.TripEntity;
import com.fasfood.tripservice.infrastructure.persistence.mapper.TripDetailsEntityMapper;
import com.fasfood.tripservice.infrastructure.persistence.mapper.TripTransitEntityMapper;
import com.fasfood.tripservice.infrastructure.persistence.repository.TripDetailsEntityRepository;
import com.fasfood.tripservice.infrastructure.persistence.repository.TripEntityRepository;
import com.fasfood.tripservice.infrastructure.persistence.repository.TripTransitEntityRepository;
import com.fasfood.web.support.AbstractDomainRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class TripRepositoryImpl extends AbstractDomainRepository<Trip, TripEntity, UUID> implements TripRepository {

    private final TripEntityRepository tripEntityRepository;
    private final TripTransitEntityRepository tripTransitEntityRepository;
    private final TripTransitEntityMapper tripTransitEntityMapper;
    private final TripDetailsEntityRepository tripDetailsEntityRepository;
    private final TripDetailsEntityMapper tripDetailsEntityMapper;

    protected TripRepositoryImpl(JpaRepository<TripEntity, UUID> jpaRepository,
                                 EntityMapper<Trip, TripEntity> mapper,
                                 TripEntityRepository tripEntityRepository,
                                 TripTransitEntityRepository tripTransitEntityRepository,
                                 TripDetailsEntityRepository tripDetailsEntityRepository,
                                 TripTransitEntityMapper tripTransitEntityMapper, TripDetailsEntityMapper tripDetailsEntityMapper) {
        super(jpaRepository, mapper);
        this.tripEntityRepository = tripEntityRepository;
        this.tripTransitEntityRepository = tripTransitEntityRepository;
        this.tripDetailsEntityRepository = tripDetailsEntityRepository;
        this.tripTransitEntityMapper = tripTransitEntityMapper;
        this.tripDetailsEntityMapper = tripDetailsEntityMapper;
    }

    @Override
    public Trip getById(UUID id) {
        Trip found = this.mapper.toDomain(this.tripEntityRepository.findById(id)
                .orElseThrow(() -> new ResponseException(NotFoundError.NOT_FOUND)));
        List<TripTransit> tripTransits = this.tripTransitEntityMapper
                .toDomain(this.tripTransitEntityRepository.findAllByTripId(id));
        List<TripDetails> tripDetails = this.tripDetailsEntityMapper
                .toDomain(this.tripDetailsEntityRepository.findAllByTripId(id));
        found.enrichTripTransits(tripTransits);
        found.enrichTripDetails(tripDetails);
        return found;
    }

    @Override
    public List<Trip> saveAll(List<Trip> domains) {
        List<TripTransit> tripTransits = new ArrayList<>();
        List<TripDetails> tripDetails = new ArrayList<>();
        domains.forEach(domain -> {
            if (!CollectionUtils.isEmpty(domain.getTripTransits())) {
                tripTransits.addAll(domain.getTripTransits());
            }
            if (!CollectionUtils.isEmpty(domain.getTripDetails())) {
                tripDetails.addAll(domain.getTripDetails());
            }
        });
        this.tripTransitEntityRepository.saveAll(this.tripTransitEntityMapper.toEntity(tripTransits));
        this.tripDetailsEntityRepository.saveAll(this.tripDetailsEntityMapper.toEntity(tripDetails));
        return super.saveAll(domains);
    }
}
