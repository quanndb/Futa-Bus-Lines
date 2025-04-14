package com.fasfood.tripservice.application.dto.mapper;

import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.tripservice.application.dto.response.BusDTO;
import com.fasfood.tripservice.domain.Bus;
import com.fasfood.tripservice.infrastructure.persistence.entity.BusEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusDTOMapper extends DTOMapper<BusDTO, Bus, BusEntity> {
}
