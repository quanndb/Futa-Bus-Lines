package com.fasfood.tripservice.domain.cmd;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteCreateOrUpDateCmd {
    private String departureCode;
    private String destinationCode;
    private Double distance;
    private Integer duration;
}
