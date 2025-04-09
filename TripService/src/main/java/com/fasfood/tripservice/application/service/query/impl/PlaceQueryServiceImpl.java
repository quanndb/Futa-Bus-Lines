package com.fasfood.tripservice.application.service.query.impl;

import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.common.mapper.QueryMapper;
import com.fasfood.commonexcel.CellConverter;
import com.fasfood.commonexcel.ExcelUtil;
import com.fasfood.persistence.custom.EntityRepository;
import com.fasfood.tripservice.application.dto.request.PlacePagingRequest;
import com.fasfood.tripservice.application.dto.response.PlaceDTO;
import com.fasfood.tripservice.application.service.query.PlaceQueryService;
import com.fasfood.tripservice.domain.Place;
import com.fasfood.tripservice.domain.query.PlacePagingQuery;
import com.fasfood.tripservice.infrastructure.persistence.entity.PlaceEntity;
import com.fasfood.tripservice.infrastructure.support.util.ExcelExtractor;
import com.fasfood.web.support.AbstractQueryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PlaceQueryServiceImpl
        extends AbstractQueryService<Place, PlaceEntity, PlaceDTO, UUID, PlacePagingQuery, PlacePagingRequest>
        implements PlaceQueryService {

    protected PlaceQueryServiceImpl(EntityRepository<PlaceEntity, UUID> entityRepository,
                                    DTOMapper<PlaceDTO, Place, PlaceEntity> dtoMapper,
                                    QueryMapper<PlacePagingQuery, PlacePagingRequest> pagingRequestMapper) {
        super(entityRepository, dtoMapper, pagingRequestMapper);
    }

    @Override
    public byte[] getTemplate() {
        return ExcelExtractor.extractPlace().exportToBytes("Places", List.of());
    }
}
