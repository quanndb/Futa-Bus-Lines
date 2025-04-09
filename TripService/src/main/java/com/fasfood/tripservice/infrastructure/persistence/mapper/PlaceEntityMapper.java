package com.fasfood.tripservice.infrastructure.persistence.mapper;

import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.tripservice.domain.Place;
import com.fasfood.tripservice.infrastructure.persistence.entity.PlaceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlaceEntityMapper extends EntityMapper<Place, PlaceEntity> {
}
