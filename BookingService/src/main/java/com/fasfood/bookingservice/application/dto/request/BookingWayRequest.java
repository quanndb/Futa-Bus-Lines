package com.fasfood.bookingservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class BookingWayRequest extends Request {
    @NotNull(message = "TRIP_REQUIRED")
    private UUID tripDetailsId;
    @NotNull(message = "DEPARTURE_DATE_REQUIRED")
    private LocalDate departureDate;
    @NotEmpty(message = "SEATS_REQUIRED")
    private List<String> seats;
    @NotNull(message = "DEPARTURE_REQUIRED")
    private UUID departureId;
    @NotNull(message = "DESTINATION_REQUIRED")
    private UUID destinationId;
}
