package com.fasfood.tripservice.application.service.query.impl;

import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.common.mapper.QueryMapper;
import com.fasfood.persistence.custom.EntityRepository;
import com.fasfood.tripservice.application.dto.request.BusPagingRequest;
import com.fasfood.tripservice.application.dto.response.BusDTO;
import com.fasfood.tripservice.application.service.query.BusQueryService;
import com.fasfood.tripservice.domain.Bus;
import com.fasfood.tripservice.domain.query.BusPagingQuery;
import com.fasfood.tripservice.infrastructure.persistence.entity.BusEntity;
import com.fasfood.tripservice.infrastructure.support.util.ExcelExtractor;
import com.fasfood.web.support.AbstractQueryService;
import com.fasfood.web.support.DomainRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BusQueryServiceImpl
        extends AbstractQueryService<Bus, BusEntity, BusDTO, UUID, BusPagingQuery, BusPagingRequest>
        implements BusQueryService {


    protected BusQueryServiceImpl(DomainRepository<Bus, UUID> domainRepository,
                                  EntityRepository<BusEntity, UUID> entityRepository,
                                  DTOMapper<BusDTO, Bus, BusEntity> dtoMapper,
                                  QueryMapper<BusPagingQuery, BusPagingRequest> pagingRequestMapper) {
        super(domainRepository, entityRepository, dtoMapper, pagingRequestMapper);
    }

    @Override
    public byte[] getTemplate() {
        return ExcelExtractor.extractBus().exportToBytes("Buses", List.of());
    }
}
