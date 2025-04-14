package com.fasfood.tripservice.infrastructure.persistence.mapper;

import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.tripservice.domain.Seat;
import com.fasfood.tripservice.infrastructure.persistence.entity.SeatEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatEntityMapper extends EntityMapper<Seat, SeatEntity> {
}
