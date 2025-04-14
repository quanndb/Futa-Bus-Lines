package com.fasfood.tripservice.application.mapper;

import com.fasfood.common.mapper.QueryMapper;
import com.fasfood.tripservice.application.dto.request.TransitPointPagingRequest;
import com.fasfood.tripservice.domain.query.TransitPointPagingQuery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransitPointQueryMapper extends QueryMapper<TransitPointPagingQuery, TransitPointPagingRequest> {
}
