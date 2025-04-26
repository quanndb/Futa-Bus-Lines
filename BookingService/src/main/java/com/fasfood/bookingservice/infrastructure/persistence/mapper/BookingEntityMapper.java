package com.fasfood.bookingservice.infrastructure.persistence.mapper;

import com.fasfood.bookingservice.domain.BookingWay;
import com.fasfood.bookingservice.infrastructure.persistence.entity.BookingEntity;
import com.fasfood.common.mapper.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingEntityMapper extends EntityMapper<BookingWay, BookingEntity> {
}
