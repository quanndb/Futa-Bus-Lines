package com.fasfood.tripservice.application.mapper;

import com.fasfood.common.mapper.QueryMapper;
import com.fasfood.tripservice.application.dto.request.BusPagingRequest;
import com.fasfood.tripservice.domain.query.BusPagingQuery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusQueryMapper extends QueryMapper<BusPagingQuery, BusPagingRequest> {
}
