package com.fasfood.tripservice.application.dto.mapper;

import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.tripservice.application.dto.response.PlaceDTO;
import com.fasfood.tripservice.domain.Place;
import com.fasfood.tripservice.infrastructure.persistence.entity.PlaceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlaceDTOMapper extends DTOMapper<PlaceDTO, Place, PlaceEntity> {
}
