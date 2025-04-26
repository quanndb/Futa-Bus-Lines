package com.fasfood.bookingservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequest extends Request {
    @NotNull(message = "DEPARTURE_TRIP_REQUIRED")
    @Valid
    private BookingWayRequest departureTrip;
    @Valid
    private BookingWayRequest returnTrip;
    @NotBlank(message = "FULL_NAME_REQUIRED")
    private String fullName;
    @NotBlank(message = "EMAIL_REQUIRED")
    @Email(message = "EMAIL_NOT_VALID")
    private String email;
    @NotBlank(message = "PHONE_NUMBER_REQUIRED")
    @Size(min = 10, max = 20, message = "PHONE_NUMBER_NOT_VALID")
    private String phone;
}
