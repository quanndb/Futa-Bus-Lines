package com.fasfood.tripservice.infrastructure.persistence.mapper;

import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.tripservice.domain.Route;
import com.fasfood.tripservice.infrastructure.persistence.entity.RouteEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RouteEntityMapper extends EntityMapper<Route, RouteEntity> {
}
