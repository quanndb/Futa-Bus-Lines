package com.fasfood.tripservice.application.mapper;

import com.fasfood.common.mapper.QueryMapper;
import com.fasfood.tripservice.application.dto.request.RoutePagingRequest;
import com.fasfood.tripservice.domain.query.RoutePagingQuery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RouteQueryMapper extends QueryMapper<RoutePagingQuery, RoutePagingRequest> {
}
