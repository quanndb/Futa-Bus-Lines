package com.fasfood.bookingservice.application.dto.response;

import com.fasfood.bookingservice.infrastructure.support.enums.BookingStatus;
import com.fasfood.bookingservice.infrastructure.support.enums.BookingType;
import com.fasfood.common.dto.AuditableDTO;
import com.fasfood.common.enums.BusTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingWayDTO extends AuditableDTO {
    private UUID userId;
    // booking
    private String code;
    private BookingStatus status;
    private BookingType type;
    private int numOfTickets;
    private String paymentLink;
    private BusTypeEnum busType;
    // user
    private String fullName;
    private String email;
    private String phone;
    // trip
    private UUID tripDetailsId;
    private LocalDate departureDate;
    private String route;
    private UUID departureId;
    private String departure;
    private String departureAddress;
    private LocalTime departureTime;
    private UUID destinationId;
    private String destination;
    private String destinationAddress;
    private LocalTime destinationTime;
    // ticket
    private Long pricePerSeat;
    private List<TicketDTO> tickets;
}
