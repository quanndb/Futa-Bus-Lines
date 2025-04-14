package com.fasfood.tripservice.application.dto.mapper;

import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.tripservice.application.dto.response.TransitPointDTO;
import com.fasfood.tripservice.domain.TransitPoint;
import com.fasfood.tripservice.infrastructure.persistence.entity.TransitPointEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransitPointDTOMapper extends DTOMapper<TransitPointDTO, TransitPoint, TransitPointEntity> {
}
