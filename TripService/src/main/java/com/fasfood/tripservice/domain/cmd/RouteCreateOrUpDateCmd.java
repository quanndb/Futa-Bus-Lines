package com.fasfood.tripservice.domain.cmd;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RouteCreateOrUpDateCmd {
    private UUID departureId;
    private UUID destinationId;
    private Double distance;
    private Integer duration;
}
