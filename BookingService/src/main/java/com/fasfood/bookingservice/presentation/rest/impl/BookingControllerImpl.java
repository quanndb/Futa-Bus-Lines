package com.fasfood.bookingservice.presentation.rest.impl;

import com.fasfood.bookingservice.application.dto.request.BookingPagingRequest;
import com.fasfood.bookingservice.application.dto.request.BookingRequest;
import com.fasfood.bookingservice.application.dto.response.BookingDTO;
import com.fasfood.bookingservice.application.service.cmd.BookingCmdService;
import com.fasfood.bookingservice.application.service.query.BookingQueryService;
import com.fasfood.bookingservice.presentation.rest.BookingController;
import com.fasfood.common.dto.request.GetBookedRequest;
import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.common.dto.response.StatisticResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BookingControllerImpl implements BookingController {

    private final BookingCmdService bookingCmdService;
    private final BookingQueryService bookingQueryService;

    @Override
    public Response<BookingDTO> send(BookingRequest request) throws JsonProcessingException {
        return Response.of(this.bookingCmdService.send(request));
    }

    @Override
    public Response<BookingDTO> getByCode(String code) {
        return Response.of(this.bookingQueryService.findByCode(code));
    }

    @Override
    public Response<BookingDTO> createPaymentLink(String code, Boolean isUseWallet) {
        return Response.of(this.bookingCmdService.createPaymentLink(code, isUseWallet));
    }


    @Override
    public Response<Void> payBooking(String code) throws JsonProcessingException {
        this.bookingCmdService.payedBooking(code);
        return Response.ok();
    }

    @Override
    public Response<BookingDTO> returnBooking(String code) {
        return Response.of(this.bookingCmdService.returnBooking(code));
    }

    @Override
    public PagingResponse<BookingDTO> getBookings(BookingPagingRequest bookingPagingRequest) {
        return PagingResponse.of(this.bookingQueryService.findBooking(bookingPagingRequest));
    }

    @Override
    public PagingResponse<BookingDTO> getMyBookings(BookingPagingRequest bookingPagingRequest) {
        return PagingResponse.of(this.bookingQueryService.findMyBooking(bookingPagingRequest));
    }

    @Override
    public Response<Map<UUID, List<String>>> getBookingSeats(GetBookedRequest request) {
        return Response.of(this.bookingQueryService.getBooked(request));
    }

    @Override
    public Response<List<String>> getBookedSeats(UUID detailsId, LocalDate departureDate) {
        return Response.of(this.bookingQueryService.getBooked(detailsId, departureDate));
    }

    @Override
    public Response<List<StatisticResponse>> getBookingStatistics(Integer year) {
        return Response.of(this.bookingQueryService.getStatistics(year));
    }

    @Override
    public Response<List<StatisticResponse>> getBookingRevenueStatistics(Integer year) {
        return Response.of(this.bookingQueryService.getRevenueStatistics(year));
    }
}
