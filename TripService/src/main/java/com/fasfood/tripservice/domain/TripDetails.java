package com.fasfood.tripservice.domain;

import com.fasfood.common.domain.Domain;
import com.fasfood.common.enums.BusTypeEnum;
import com.fasfood.tripservice.domain.cmd.TripDetailsCreateOrUpdateCmd;
import com.fasfood.tripservice.infrastructure.support.enums.TripStatus;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class TripDetails extends Domain {
    private UUID tripId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private BusTypeEnum type;
    private long price;
    private TripStatus status;

    public TripDetails(UUID tripId, TripDetailsCreateOrUpdateCmd cmd) {
        super();
        this.tripId = tripId;
        this.fromDate = cmd.getFromDate();
        this.toDate = cmd.getToDate();
        this.type = cmd.getType();
        this.price = cmd.getPrice();
        this.status = cmd.getStatus();
    }

    public TripDetails update(TripDetailsCreateOrUpdateCmd cmd) {
        this.fromDate = cmd.getFromDate();
        this.toDate = cmd.getToDate();
        this.price = cmd.getPrice();
        this.status = cmd.getStatus();
        return this;
    }
}
