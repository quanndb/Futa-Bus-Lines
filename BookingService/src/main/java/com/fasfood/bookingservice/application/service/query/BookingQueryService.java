package com.fasfood.bookingservice.application.service.query;

import com.fasfood.bookingservice.application.dto.request.BookingPagingRequest;
import com.fasfood.bookingservice.application.dto.response.BookingDTO;
import com.fasfood.common.dto.PageDTO;
import com.fasfood.common.dto.request.GetBookedRequest;
import com.fasfood.common.dto.response.StatisticResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface BookingQueryService {
    BookingDTO findByCode(String code);

    PageDTO<BookingDTO> findBooking(BookingPagingRequest request);

    PageDTO<BookingDTO> findMyBooking(BookingPagingRequest request);

    Map<UUID, List<String>> getBooked(GetBookedRequest request);

    List<String> getBooked(UUID detailsId, LocalDate departureDate);

    List<StatisticResponse> getStatistics(Integer year);

    List<StatisticResponse> getRevenueStatistics(Integer year);
}