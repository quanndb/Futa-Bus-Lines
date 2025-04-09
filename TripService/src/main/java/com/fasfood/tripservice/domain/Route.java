package com.fasfood.tripservice.domain;

import com.fasfood.common.domain.Domain;
import com.fasfood.tripservice.domain.cmd.RouteCreateOrUpDateCmd;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class Route extends Domain {
    private String departureCode;
    private String destinationCode;
    private Double distance;
    private Integer duration;

    public Route(RouteCreateOrUpDateCmd cmd) {
        super();
        this.departureCode = cmd.getDepartureCode();
        this.destinationCode = cmd.getDestinationCode();
        this.distance = cmd.getDistance();
        this.duration = cmd.getDuration();
    }

    public Route update(RouteCreateOrUpDateCmd cmd) {
        this.departureCode = cmd.getDepartureCode();
        this.destinationCode = cmd.getDestinationCode();
        this.distance = cmd.getDistance();
        this.duration = cmd.getDuration();
        return this;
    }
}
