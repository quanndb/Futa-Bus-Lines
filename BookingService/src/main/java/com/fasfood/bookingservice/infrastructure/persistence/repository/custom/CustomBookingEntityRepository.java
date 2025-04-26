package com.fasfood.bookingservice.infrastructure.persistence.repository.custom;

import com.fasfood.bookingservice.domain.query.BookingPagingQuery;

import java.util.List;

public interface CustomBookingEntityRepository {
    List<String> searchCode(BookingPagingQuery query);
}
