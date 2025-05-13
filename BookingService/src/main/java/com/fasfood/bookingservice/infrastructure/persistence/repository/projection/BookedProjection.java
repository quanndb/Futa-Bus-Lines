package com.fasfood.bookingservice.infrastructure.persistence.repository.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookedProjection {
    UUID tripDetailsId;
    String seatNumber;
}
