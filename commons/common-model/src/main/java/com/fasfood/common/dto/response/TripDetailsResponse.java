package com.fasfood.common.dto.response;

import com.fasfood.common.enums.BusTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripDetailsResponse {
    private UUID tripDetailsId;
    private LocalDate departureDate;
    // ticket
    private Long pricePerSeat;
    private List<String> seats;
    // enrich
    private String route;
    private BusTypeEnum type;
    private UUID departureId;
    private String departure;
    private LocalTime departureTime;
    private String departureAddress;
    private UUID destinationId;
    private String destination;
    private LocalTime destinationTime;
    private String destinationAddress;
}
