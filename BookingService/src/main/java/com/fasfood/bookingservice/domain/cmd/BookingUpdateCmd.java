package com.fasfood.bookingservice.domain.cmd;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingUpdateCmd {
    private BookingWayUpdateCmd departureTrip;
    private BookingWayUpdateCmd returnTrip;
    private String fullName;
    private String email;
    private String phone;
}
