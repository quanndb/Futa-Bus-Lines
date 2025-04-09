package com.fasfood.tripservice.application.mapper;

import com.fasfood.common.mapper.QueryMapper;
import com.fasfood.tripservice.application.dto.request.PlacePagingRequest;
import com.fasfood.tripservice.domain.query.PlacePagingQuery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlaceQueryMapper extends QueryMapper<PlacePagingQuery, PlacePagingRequest> {
}
