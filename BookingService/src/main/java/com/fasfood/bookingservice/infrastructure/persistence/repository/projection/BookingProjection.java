package com.fasfood.bookingservice.infrastructure.persistence.repository.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingProjection {
    private String code;
    private Instant date;

    public BookingProjection(String code, Object date) {
        this.code = code;
        this.date = (Instant) date;
    }
}
