package com.fasfood.bookingservice.application.service.cmd;

import com.fasfood.bookingservice.application.dto.request.BookingRequest;
import com.fasfood.bookingservice.application.dto.response.BookingDTO;
import com.fasfood.bookingservice.domain.Booking;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface BookingCmdService {
    void send(BookingRequest request) throws JsonProcessingException;

    BookingDTO consumeBooking(Booking booking);

    void payBooking(String code) throws JsonProcessingException;

    BookingDTO returnBooking(String code);

    BookingDTO createPaymentLink(String code, boolean isUseWallet);
}