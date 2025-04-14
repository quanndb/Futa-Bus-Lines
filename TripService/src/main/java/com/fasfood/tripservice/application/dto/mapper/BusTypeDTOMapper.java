package com.fasfood.tripservice.application.dto.mapper;

import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.common.dto.response.BusTypeDTO;
import com.fasfood.tripservice.domain.BusType;
import com.fasfood.tripservice.infrastructure.persistence.entity.BusTypeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusTypeDTOMapper extends DTOMapper<BusTypeDTO, BusType, BusTypeEntity> {
}
