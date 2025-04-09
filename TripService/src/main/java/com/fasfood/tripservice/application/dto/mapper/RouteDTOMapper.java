package com.fasfood.tripservice.application.dto.mapper;

import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.tripservice.application.dto.response.RouteDTO;
import com.fasfood.tripservice.domain.Route;
import com.fasfood.tripservice.infrastructure.persistence.entity.RouteEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RouteDTOMapper extends DTOMapper<RouteDTO, Route, RouteEntity> {
}
