package com.fasfood.bookingservice.application.service.cmd;

import com.fasfood.bookingservice.application.dto.request.BookingRequest;
import com.fasfood.bookingservice.application.dto.response.BookingDTO;
import com.fasfood.bookingservice.domain.Booking;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface BookingCmdService {
    BookingDTO send(BookingRequest request) throws JsonProcessingException;

    BookingDTO consumeBooking(Booking booking);

    void payedBooking(String code) throws JsonProcessingException;

    void consumePayedBooking(Booking booking) throws JsonProcessingException;

    BookingDTO returnBooking(String code);

    BookingDTO createPaymentLink(String code, boolean isUseWallet);
}