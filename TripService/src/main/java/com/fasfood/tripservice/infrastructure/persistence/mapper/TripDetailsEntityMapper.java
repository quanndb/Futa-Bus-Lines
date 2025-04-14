package com.fasfood.tripservice.infrastructure.persistence.mapper;

import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.tripservice.domain.TripDetails;
import com.fasfood.tripservice.infrastructure.persistence.entity.TripDetailsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TripDetailsEntityMapper extends EntityMapper<TripDetails, TripDetailsEntity> {
}
