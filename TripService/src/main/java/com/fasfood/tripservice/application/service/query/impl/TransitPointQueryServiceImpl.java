package com.fasfood.tripservice.application.service.query.impl;

import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.common.mapper.QueryMapper;
import com.fasfood.persistence.custom.EntityRepository;
import com.fasfood.tripservice.application.dto.request.TransitPointPagingRequest;
import com.fasfood.tripservice.application.dto.response.TransitPointDTO;
import com.fasfood.tripservice.application.service.query.TransitPointQueryService;
import com.fasfood.tripservice.domain.TransitPoint;
import com.fasfood.tripservice.domain.query.TransitPointPagingQuery;
import com.fasfood.tripservice.infrastructure.persistence.entity.TransitPointEntity;
import com.fasfood.tripservice.infrastructure.support.util.ExcelExtractor;
import com.fasfood.web.support.AbstractQueryService;
import com.fasfood.web.support.DomainRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransitPointQueryServiceImpl
        extends AbstractQueryService<TransitPoint, TransitPointEntity, TransitPointDTO, UUID, TransitPointPagingQuery, TransitPointPagingRequest>
        implements TransitPointQueryService {

    protected TransitPointQueryServiceImpl(DomainRepository<TransitPoint, UUID> domainRepository,
                                           EntityRepository<TransitPointEntity, UUID> entityRepository,
                                           DTOMapper<TransitPointDTO, TransitPoint, TransitPointEntity> dtoMapper,
                                           QueryMapper<TransitPointPagingQuery, TransitPointPagingRequest> pagingRequestMapper) {
        super(domainRepository, entityRepository, dtoMapper, pagingRequestMapper);
    }

    @Override
    public byte[] getTemplate() {
        return ExcelExtractor.extractTransitPoint().exportToBytes("Transit Points", List.of());
    }
}
