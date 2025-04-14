package com.fasfood.tripservice.application.mapper;

import com.fasfood.common.mapper.QueryMapper;
import com.fasfood.tripservice.application.dto.request.TripPagingRequest;
import com.fasfood.tripservice.domain.query.TripPagingQuery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TripQueryMapper extends QueryMapper<TripPagingQuery, TripPagingRequest> {
}
