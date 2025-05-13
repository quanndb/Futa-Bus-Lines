package com.fasfood.tripservice.domain;

import com.fasfood.common.domain.Domain;
import com.fasfood.common.enums.BusFloor;
import com.fasfood.common.enums.BusTypeEnum;
import com.fasfood.tripservice.domain.cmd.BusTypeCreateOrUpdateCmd;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class BusType extends Domain {
    private BusTypeEnum type;
    private int seatCapacity;
    private List<Seat> firstFloorSeats;
    private List<Seat> secondFloorSeats;

    public BusType(BusTypeCreateOrUpdateCmd cmd) {
        super();
        this.type = cmd.getType();
        this.firstFloorSeats = this.createSeats(cmd.getFirstFloorSeats(), BusFloor.FIRST);
        this.secondFloorSeats = this.createSeats(cmd.getSecondFloorSeats(), BusFloor.SECOND);
        this.seatCapacity = this.calculateSeatCapacity(BusFloor.FIRST) + this.calculateSeatCapacity(BusFloor.SECOND);
    }

    public BusType update(BusTypeCreateOrUpdateCmd cmd) {
        this.updateSeats(cmd.getFirstFloorSeats(), BusFloor.FIRST);
        this.updateSeats(cmd.getSecondFloorSeats(), BusFloor.SECOND);
        this.seatCapacity = this.calculateSeatCapacity(BusFloor.FIRST) + this.calculateSeatCapacity(BusFloor.SECOND);
        return this;
    }

    private void updateSeats(List<String> seats, BusFloor floor) {
        if (CollectionUtils.isEmpty(seats)) return;
        if (BusFloor.FIRST.equals(floor)) {
            this.firstFloorSeats.forEach(Seat::delete);
            this.firstFloorSeats.addAll(this.createSeats(seats, floor));
            return;
        }
        this.secondFloorSeats.forEach(Seat::delete);
        this.secondFloorSeats.addAll(this.createSeats(seats, floor));
    }

    private List<Seat> createSeats(List<String> seats, BusFloor floor) {
        if (CollectionUtils.isEmpty(seats)) return List.of();
        List<Seat> seatList = new ArrayList<>();
        for (int i = 0; i < seats.size(); i++) {
            seatList.add(new Seat(this.id, seats.get(i), floor, i));
        }
        return seatList;
    }

    public void enrichSeats(List<Seat> seats) {
        this.firstFloorSeats = new ArrayList<>();
        this.secondFloorSeats = new ArrayList<>();
        if (CollectionUtils.isEmpty(seats)) return;
        seats.forEach(seat -> {
            if (BusFloor.FIRST.equals(seat.getFloor())) {
                this.firstFloorSeats.add(seat);
            } else {
                this.secondFloorSeats.add(seat);
            }
        });
    }

    private int calculateSeatCapacity(BusFloor floor) {
        int result = 0;
        if (BusFloor.FIRST.equals(floor)) {
            for (Seat seat : this.firstFloorSeats) {
                if (Objects.nonNull(seat.getSeatNumber()) && !seat.getDeleted()) {
                    result++;
                }
            }
        } else if (BusFloor.SECOND.equals(floor)) {
            for (Seat seat : this.secondFloorSeats) {
                if (Objects.nonNull(seat.getSeatNumber()) && !seat.getDeleted()) {
                    result++;
                }
            }
        }
        return result;
    }
}
