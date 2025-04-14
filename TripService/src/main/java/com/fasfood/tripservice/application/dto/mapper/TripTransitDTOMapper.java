package com.fasfood.tripservice.application.dto.mapper;

import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.tripservice.application.dto.response.TripTransitDTO;
import com.fasfood.tripservice.domain.TripTransit;
import com.fasfood.tripservice.infrastructure.persistence.entity.TripTransitEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TripTransitDTOMapper extends DTOMapper<TripTransitDTO, TripTransit, TripTransitEntity> {
}
