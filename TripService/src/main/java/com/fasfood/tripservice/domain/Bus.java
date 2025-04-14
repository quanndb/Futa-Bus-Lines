package com.fasfood.tripservice.domain;

import com.fasfood.common.domain.Domain;
import com.fasfood.tripservice.domain.cmd.BusCreateOrUpdateCmd;
import com.fasfood.tripservice.infrastructure.support.enums.BusStatus;
import com.fasfood.common.enums.BusTypeEnum;
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
public class Bus extends Domain {
    private String licensePlate;
    private BusStatus status;
    private BusTypeEnum type;

    public Bus(BusCreateOrUpdateCmd cmd) {
        super();
        this.licensePlate = cmd.getLicensePlate();
        this.status = BusStatus.ACTIVE;
        this.type = cmd.getType();
    }

    public Bus update(BusCreateOrUpdateCmd cmd) {
        this.licensePlate = cmd.getLicensePlate();
        this.type = cmd.getType();
        this.status = cmd.getStatus();
        return this;
    }
}
