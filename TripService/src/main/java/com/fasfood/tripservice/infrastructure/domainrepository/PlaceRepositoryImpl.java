package com.fasfood.tripservice.infrastructure.domainrepository;

import com.fasfood.common.error.NotFoundError;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.tripservice.domain.Place;
import com.fasfood.tripservice.domain.repository.PlaceRepository;
import com.fasfood.tripservice.infrastructure.persistence.entity.PlaceEntity;
import com.fasfood.tripservice.infrastructure.persistence.mapper.PlaceEntityMapper;
import com.fasfood.tripservice.infrastructure.persistence.repository.PlaceEntityRepository;
import com.fasfood.web.support.AbstractDomainRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class PlaceRepositoryImpl extends AbstractDomainRepository<Place, PlaceEntity, UUID> implements PlaceRepository {

    private final PlaceEntityRepository placeEntityRepository;
    private final PlaceEntityMapper placeEntityMapper;

    protected PlaceRepositoryImpl(JpaRepository<PlaceEntity, UUID> jpaRepository,
                                  EntityMapper<Place, PlaceEntity> mapper,
                                  PlaceEntityRepository placeEntityRepository, PlaceEntityMapper placeEntityMapper) {
        super(jpaRepository, mapper);
        this.placeEntityRepository = placeEntityRepository;
        this.placeEntityMapper = placeEntityMapper;
    }

    @Override
    public Place getById(UUID id) {
        PlaceEntity entity = this.placeEntityRepository.findById(id)
                .orElseThrow(() -> new ResponseException(NotFoundError.NOT_FOUND));
        return this.placeEntityMapper.toDomain(entity);
    }
}
