package com.fasfood.bookingservice.domain.cmd;

import com.fasfood.common.enums.BusTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class BookingWayCreateCmd {
    private UUID tripDetailsId;
    private LocalDate departureDate;
    // ticket
    private Long pricePerSeat;
    private List<String> seats;
    private BusTypeEnum type;
    //enrich
    private String route;
    private UUID departureId;
    private String departure;
    private LocalTime departureTime;
    private String departureAddress;
    private UUID destinationId;
    private String destination;
    private LocalTime destinationTime;
    private String destinationAddress;
}
