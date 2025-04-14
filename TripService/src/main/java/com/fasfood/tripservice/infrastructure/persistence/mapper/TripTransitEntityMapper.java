package com.fasfood.tripservice.infrastructure.persistence.mapper;

import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.tripservice.domain.TripTransit;
import com.fasfood.tripservice.infrastructure.persistence.entity.TripTransitEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TripTransitEntityMapper extends EntityMapper<TripTransit, TripTransitEntity> {
}
