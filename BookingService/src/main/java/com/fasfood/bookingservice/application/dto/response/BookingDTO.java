package com.fasfood.bookingservice.application.dto.response;

import com.fasfood.bookingservice.infrastructure.support.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
public class BookingDTO {
    private BookingWayDTO departureTrip;
    private BookingWayDTO returnTrip;
    private String code;
    private BookingStatus status;
    private String paymentLink;
    private String departureRoute;
    private String returnRoute;
    private LocalDateTime departureTime;
    private LocalDateTime returnTime;
    private Long totalPrice;
    private int numOfTickets;
    private Instant createdAt;
}
