package com.fasfood.tripservice.infrastructure.domainrepository;

import com.fasfood.common.error.NotFoundError;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.tripservice.domain.Route;
import com.fasfood.tripservice.domain.repository.RouteRepository;
import com.fasfood.tripservice.infrastructure.persistence.entity.RouteEntity;
import com.fasfood.tripservice.infrastructure.persistence.repository.RouteEntityRepository;
import com.fasfood.web.support.AbstractDomainRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class RouteRepositoryImpl extends AbstractDomainRepository<Route, RouteEntity, UUID> implements RouteRepository {

    private final RouteEntityRepository routeEntityRepository;

    protected RouteRepositoryImpl(JpaRepository<RouteEntity, UUID> jpaRepository,
                                  EntityMapper<Route, RouteEntity> mapper, RouteEntityRepository routeEntityRepository) {
        super(jpaRepository, mapper);
        this.routeEntityRepository = routeEntityRepository;
    }

    @Override
    public Route getById(UUID id) {
        return this.mapper.toDomain(this.routeEntityRepository.findById(id)
                .orElseThrow(() -> new ResponseException(NotFoundError.NOT_FOUND)));
    }
}
