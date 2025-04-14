package com.fasfood.tripservice.infrastructure.persistence.mapper;

import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.tripservice.domain.TransitPoint;
import com.fasfood.tripservice.infrastructure.persistence.entity.TransitPointEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransitPointEntityMapper extends EntityMapper<TransitPoint, TransitPointEntity> {
}
