package com.fasfood.bookingservice.infrastructure.persistence.repository.custom;

import com.fasfood.bookingservice.application.dto.request.BookingStatisticRequest;
import com.fasfood.bookingservice.domain.query.BookingPagingQuery;
import com.fasfood.bookingservice.infrastructure.persistence.repository.projection.BookedProjection;
import com.fasfood.common.dto.request.GetBookedRequest;
import com.fasfood.common.dto.response.StatisticResponse;

import java.util.List;

public interface CustomBookingEntityRepository {
    List<String> searchCode(BookingPagingQuery query);

    List<BookedProjection> getBooked(GetBookedRequest request);
}
