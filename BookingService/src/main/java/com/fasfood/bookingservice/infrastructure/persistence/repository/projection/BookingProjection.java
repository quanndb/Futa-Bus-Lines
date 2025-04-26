package com.fasfood.bookingservice.infrastructure.persistence.repository.projection;

import java.util.UUID;

public interface BookingProjection {
    UUID getTripDetailsId();
    String getSeatNumber();
}
