package com.fasfood.tripservice.infrastructure.persistence.mapper;

import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.tripservice.domain.BusType;
import com.fasfood.tripservice.infrastructure.persistence.entity.BusTypeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusTypeEntityMapper extends EntityMapper<BusType, BusTypeEntity> {
}
