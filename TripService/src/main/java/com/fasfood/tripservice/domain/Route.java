package com.fasfood.tripservice.domain;

import com.fasfood.common.domain.Domain;
import com.fasfood.tripservice.domain.cmd.RouteCreateOrUpDateCmd;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class Route extends Domain {
    private UUID departureId;
    private UUID destinationId;
    private Double distance;
    private Integer duration;

    public Route(RouteCreateOrUpDateCmd cmd) {
        super();
        this.departureId = cmd.getDepartureId();
        this.destinationId = cmd.getDestinationId();
        this.distance = cmd.getDistance();
        this.duration = cmd.getDuration();
    }

    public Route update(RouteCreateOrUpDateCmd cmd) {
        this.departureId = cmd.getDepartureId();
        this.destinationId = cmd.getDestinationId();
        this.distance = cmd.getDistance();
        this.duration = cmd.getDuration();
        return this;
    }
}
