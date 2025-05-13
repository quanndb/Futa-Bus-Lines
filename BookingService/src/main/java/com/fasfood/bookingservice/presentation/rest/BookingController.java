package com.fasfood.bookingservice.presentation.rest;

import com.fasfood.bookingservice.application.dto.request.BookingPagingRequest;
import com.fasfood.bookingservice.application.dto.request.BookingRequest;
import com.fasfood.bookingservice.application.dto.response.BookingDTO;
import com.fasfood.common.dto.request.GetBookedRequest;
import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.common.dto.response.StatisticResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Tag(name = "Booking resource")
@RequestMapping("/api/v1")
@Validated
public interface BookingController {
    @Operation(summary = "Send booking")
    @PostMapping(value = "/bookings")
    Response<BookingDTO> send(@RequestBody @Valid BookingRequest request) throws JsonProcessingException;

    @Operation(summary = "Get booking")
    @GetMapping(value = "/bookings/details")
    Response<BookingDTO> getByCode(@RequestParam String code);

    @Operation(summary = "Create payment link")
    @PostMapping(value = "/bookings/payment")
    Response<BookingDTO> createPaymentLink(@RequestParam String code, @RequestParam Boolean isUseWallet);

    @Operation(summary = "Pay the booking")
    @PostMapping(value = "/bookings/payed")
    @PreAuthorize("hasPermission(null, 'client.update')")
    Response<Void> payBooking(@RequestParam String code) throws JsonProcessingException;

    @Operation(summary = "Return the booking")
    @PostMapping(value = "/bookings/return")
    Response<BookingDTO> returnBooking(@RequestParam String code);

    @Operation(summary = "Get bookings")
    @GetMapping(value = "/bookings")
    @PreAuthorize("hasPermission(null, 'bookings.read')")
    PagingResponse<BookingDTO> getBookings(@ParameterObject BookingPagingRequest bookingPagingRequest);

    @Operation(summary = "Get my bookings")
    @GetMapping(value = "/me/bookings")
    PagingResponse<BookingDTO> getMyBookings(@ParameterObject BookingPagingRequest bookingPagingRequest);

    @Operation(summary = "Get booking seats")
    @GetMapping(value = "/booking-seats")
    Response<Map<UUID, List<String>>> getBookingSeats(@ParameterObject @Valid GetBookedRequest request);

    @Operation(summary = "Get booking seats")
    @GetMapping(value = "/booking-seats/{detailsId}")
    Response<List<String>> getBookedSeats(@PathVariable UUID detailsId, @RequestParam LocalDate departureDate);

    @Operation(summary = "Get bookings statistics")
    @GetMapping(value = "/bookings/statistics")
    @PreAuthorize("hasPermission(null, 'bookings.read')")
    Response<List<StatisticResponse>> getBookingStatistics(@RequestParam(required = false) Integer year);

    @Operation(summary = "Get bookings revenue statistics")
    @GetMapping(value = "/bookings/revenue-statistics")
    @PreAuthorize("hasPermission(null, 'bookings.read')")
    Response<List<StatisticResponse>> getBookingRevenueStatistics(@RequestParam(required = false) Integer year);
}
