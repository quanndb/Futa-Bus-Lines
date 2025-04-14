package com.fasfood.tripservice.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class TripResponse {
    private UUID id;
    private TripDetailsDTO details;
    private UUID tripId;
    private List<TripTransitDTO> tripTransits;
}
