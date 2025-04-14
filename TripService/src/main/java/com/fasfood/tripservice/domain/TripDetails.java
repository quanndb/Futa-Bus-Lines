package com.fasfood.tripservice.domain;

import com.fasfood.common.domain.Domain;
import com.fasfood.tripservice.domain.cmd.TripDetailsCreateOrUpdateCmd;
import com.fasfood.common.enums.BusTypeEnum;
import com.fasfood.tripservice.infrastructure.support.enums.TripStatus;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class TripDetails extends Domain {
    private UUID tripId;
    private Instant fromAt;
    private Instant toAt;
    private UUID driverId;
    private UUID assistantId;
    private BusTypeEnum type;
    private UUID busId;
    private long price;
    private TripStatus status;

    public TripDetails(UUID tripId, TripDetailsCreateOrUpdateCmd cmd) {
        super();
        this.tripId = tripId;
        this.fromAt = cmd.getFromAt();
        this.toAt = cmd.getToAt();
        this.driverId = cmd.getDriverId();
        this.assistantId = cmd.getAssistantId();
        this.type = cmd.getType();
        this.busId = cmd.getBusId();
        this.price = cmd.getPrice();
        this.status = cmd.getStatus();
    }

    public TripDetails update(TripDetailsCreateOrUpdateCmd cmd) {
        this.fromAt = cmd.getFromAt();
        this.toAt = cmd.getToAt();
        this.driverId = cmd.getDriverId();
        this.assistantId = cmd.getAssistantId();
        this.busId = cmd.getBusId();
        this.price = cmd.getPrice();
        this.status = cmd.getStatus();
        return this;
    }
}
