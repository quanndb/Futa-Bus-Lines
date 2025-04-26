package com.fasfood.bookingservice.domain.cmd;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class BookingWayUpdateCmd {
    //enrich
    private String departure;
    private LocalTime departureTime;
    private String departureAddress;
    private String destination;
    private LocalTime destinationTime;
    private String destinationAddress;
}
