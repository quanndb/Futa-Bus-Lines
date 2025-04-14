package com.fasfood.tripservice.domain;

import com.fasfood.common.domain.Domain;
import com.fasfood.common.enums.BusFloor;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class Seat extends Domain {
    private UUID typeId;
    private String seatNumber;
    private BusFloor floor;

    public Seat(UUID typeId, String seatNumber, BusFloor floor) {
        super();
        this.typeId = typeId;
        this.seatNumber = seatNumber;
        this.floor = floor;
    }

    public Seat update(String seatNumber) {
        this.seatNumber = seatNumber;
        return this;
    }
}
