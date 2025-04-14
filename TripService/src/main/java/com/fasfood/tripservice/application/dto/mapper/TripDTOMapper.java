package com.fasfood.tripservice.application.dto.mapper;

import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.tripservice.application.dto.response.TripDTO;
import com.fasfood.tripservice.domain.Trip;
import com.fasfood.tripservice.infrastructure.persistence.entity.TripEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TripDTOMapper extends DTOMapper<TripDTO, Trip, TripEntity> {
}
