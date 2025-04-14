package com.fasfood.tripservice.infrastructure.persistence.mapper;

import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.tripservice.domain.Trip;
import com.fasfood.tripservice.infrastructure.persistence.entity.TripEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TripEntityMapper extends EntityMapper<Trip, TripEntity> {
}
