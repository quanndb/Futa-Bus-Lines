package com.fasfood.tripservice.domain.cmd;

import com.fasfood.tripservice.infrastructure.support.enums.TripTransitType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
public class TripTransitCreateOrUpdateCmd {
    private UUID transitPointId;
    private LocalTime arrivalTime;
    private TripTransitType type;
}
