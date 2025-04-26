package com.fasfood.bookingservice.domain.cmd;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingCreateCmd {
    private BookingWayCreateCmd departureTrip;
    private BookingWayCreateCmd returnTrip;
    private String fullName;
    private String email;
    private String phone;
}
