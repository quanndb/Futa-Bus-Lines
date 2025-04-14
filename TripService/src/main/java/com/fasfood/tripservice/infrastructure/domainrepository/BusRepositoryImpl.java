package com.fasfood.tripservice.infrastructure.domainrepository;

import com.fasfood.common.error.NotFoundError;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.tripservice.domain.Bus;
import com.fasfood.tripservice.domain.repository.BusRepository;
import com.fasfood.tripservice.infrastructure.persistence.entity.BusEntity;
import com.fasfood.tripservice.infrastructure.persistence.repository.BusEntityRepository;
import com.fasfood.web.support.AbstractDomainRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class BusRepositoryImpl extends AbstractDomainRepository<Bus, BusEntity, UUID> implements BusRepository {

    private final BusEntityRepository busEntityRepository;

    protected BusRepositoryImpl(JpaRepository<BusEntity, UUID> jpaRepository,
                                EntityMapper<Bus, BusEntity> mapper,
                                BusEntityRepository busEntityRepository) {
        super(jpaRepository, mapper);
        this.busEntityRepository = busEntityRepository;
    }

    @Override
    public Bus getById(UUID id) {
        return this.mapper.toDomain(this.busEntityRepository.findById(id)
                .orElseThrow(() -> new ResponseException(NotFoundError.NOT_FOUND)));
    }
}
