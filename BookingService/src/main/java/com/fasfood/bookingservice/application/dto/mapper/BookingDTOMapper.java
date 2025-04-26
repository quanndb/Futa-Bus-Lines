package com.fasfood.bookingservice.application.dto.mapper;

import com.fasfood.bookingservice.application.dto.response.BookingDTO;
import com.fasfood.bookingservice.application.dto.response.BookingWayDTO;
import com.fasfood.bookingservice.domain.Booking;
import com.fasfood.bookingservice.domain.BookingWay;
import com.fasfood.bookingservice.infrastructure.persistence.entity.BookingEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingDTOMapper {
    BookingDTO from(Booking booking);

    List<BookingDTO> from(List<Booking> bookings);

    List<BookingWayDTO> fromDomains(List<BookingWay> bookings);

    List<BookingWay> fromEntities(List<BookingEntity> bookings);
}
