package com.fasfood.bookingservice.application.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TicketDTO {
    private String seatNumber;
    private UUID bookingId;
}
