package com.fasfood.tripservice.domain;

import com.fasfood.common.domain.Domain;
import com.fasfood.tripservice.domain.cmd.TripTransitCreateOrUpdateCmd;
import com.fasfood.tripservice.infrastructure.support.enums.TripTransitType;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class TripTransit extends Domain {
    private UUID tripId;
    private UUID transitPointId;
    private LocalTime arrivalTime;
    private int transitOrder;
    private TripTransitType type;

    public TripTransit(UUID tripId, int transitOrder, TripTransitCreateOrUpdateCmd cmd) {
        super();
        this.tripId = tripId;
        this.arrivalTime = cmd.getArrivalTime();
        this.transitPointId = cmd.getTransitPointId();
        this.transitOrder = transitOrder;
        this.type = cmd.getType();
    }
}
