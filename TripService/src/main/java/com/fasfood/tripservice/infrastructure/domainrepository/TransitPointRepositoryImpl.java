package com.fasfood.tripservice.infrastructure.domainrepository;

import com.fasfood.common.error.NotFoundError;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.tripservice.domain.TransitPoint;
import com.fasfood.tripservice.domain.repository.TransitPointRepository;
import com.fasfood.tripservice.infrastructure.persistence.entity.TransitPointEntity;
import com.fasfood.tripservice.infrastructure.persistence.repository.TransitPointEntityRepository;
import com.fasfood.web.support.AbstractDomainRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class TransitPointRepositoryImpl
        extends AbstractDomainRepository<TransitPoint, TransitPointEntity, UUID>
        implements TransitPointRepository {

    private final TransitPointEntityRepository transitPointEntityRepository;

    protected TransitPointRepositoryImpl(JpaRepository<TransitPointEntity, UUID> jpaRepository,
                                         EntityMapper<TransitPoint, TransitPointEntity> mapper,
                                         TransitPointEntityRepository transitPointEntityRepository) {
        super(jpaRepository, mapper);
        this.transitPointEntityRepository = transitPointEntityRepository;
    }

    @Override
    public TransitPoint getById(UUID id) {
        return this.mapper.toDomain(this.transitPointEntityRepository.findById(id)
                .orElseThrow(() -> new ResponseException(NotFoundError.NOT_FOUND)));
    }
}
