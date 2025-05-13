package com.fasfood.tripservice.application.dto.response;

import com.fasfood.common.dto.response.SeatDTO;
import com.fasfood.common.enums.BusTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class TripDetailsTransitDTO {
    private UUID tripDetailsId;
    private LocalDate departureDate;
    // ticket
    private Long pricePerSeat;
    private List<SeatDTO> firstFloorSeats;
    private List<SeatDTO> secondFloorSeats;
    // enrich
    private BusTypeEnum type;
    private String departure;
    private LocalTime departureTime;
    private String destination;
    private LocalTime destinationTime;
    private List<TripTransitDTO> transitPoints;
}
