package com.fasfood.tripservice.infrastructure.persistence.mapper;

import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.tripservice.domain.Bus;
import com.fasfood.tripservice.infrastructure.persistence.entity.BusEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusEntityMapper extends EntityMapper<Bus, BusEntity> {
}
