package com.fasfood.tripservice.application.dto.mapper;

import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.tripservice.application.dto.response.TripDetailsDTO;
import com.fasfood.tripservice.domain.TripDetails;
import com.fasfood.tripservice.infrastructure.persistence.entity.TripDetailsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TripDetailsDTOMapper extends DTOMapper<TripDetailsDTO, TripDetails, TripDetailsEntity> {
}
