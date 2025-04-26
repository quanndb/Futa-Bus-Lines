package com.fasfood.bookingservice.domain;

import com.fasfood.common.domain.Domain;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class Ticket extends Domain {
    private String seatNumber;
    private UUID bookingId;

    public Ticket(UUID bookingId, String seatNumber) {
        this.bookingId = bookingId;
        this.seatNumber = seatNumber;
    }
}
