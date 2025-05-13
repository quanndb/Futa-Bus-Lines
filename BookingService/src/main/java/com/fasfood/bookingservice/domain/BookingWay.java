package com.fasfood.bookingservice.domain;

import com.fasfood.bookingservice.domain.cmd.BookingCreateCmd;
import com.fasfood.bookingservice.domain.cmd.BookingWayCreateCmd;
import com.fasfood.bookingservice.infrastructure.support.enums.BookingStatus;
import com.fasfood.bookingservice.infrastructure.support.enums.BookingType;
import com.fasfood.bookingservice.infrastructure.support.exception.BadRequestError;
import com.fasfood.common.domain.Domain;
import com.fasfood.common.enums.BusTypeEnum;
import com.fasfood.common.exception.ResponseException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class BookingWay extends Domain {
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
    private List<Ticket> tickets;


    public BookingWay(UUID userId, String code, BookingType type, BookingCreateCmd bkCmd, BookingWayCreateCmd wayCmd) {
        this.userId = userId;
        // booking
        this.code = code;
        this.status = BookingStatus.OUT_OF_PAY;
        this.type = type;
        // user
        this.fullName = bkCmd.getFullName();
        this.email = bkCmd.getEmail();
        this.phone = bkCmd.getPhone();
        // trip
        this.tripDetailsId = wayCmd.getTripDetailsId();
        this.departureDate = wayCmd.getDepartureDate();
        this.route = wayCmd.getRoute();
        this.departureId = wayCmd.getDepartureId();
        this.departure = wayCmd.getDeparture();
        this.departureAddress = wayCmd.getDepartureAddress();
        this.departureTime = wayCmd.getDepartureTime();
        this.destinationId = wayCmd.getDestinationId();
        this.destination = wayCmd.getDestination();
        this.destinationAddress = wayCmd.getDestinationAddress();
        this.destinationTime = wayCmd.getDestinationTime();
        this.busType = wayCmd.getType();
        // ticket
        this.pricePerSeat = wayCmd.getPricePerSeat();
        this.tickets = this.createTicket(wayCmd.getSeats());
        this.numOfTickets = calculateNumOfTickets(this);
    }

    public void createPaymentLink(String paymentLink) {
        if(!BookingStatus.WAIT_TO_PAY.equals(this.status)) {
            throw new ResponseException(BadRequestError.OUT_OF_PAY);
        }
        this.paymentLink = paymentLink;
    }

//    public void update(BookingUpdateCmd bkCmd, BookingWayUpdateCmd wayCmd) {
//        if (LocalDateTime.now().isAfter(this.departureDate.atTime(this.departureTime))) {
//            throw new ResponseException(BadRequestError.UNABLE_TO_UPDATE_WHILE_RUNNING);
//        }
//        // user
//        this.fullName = bkCmd.getFullName();
//        this.email = bkCmd.getEmail();
//        this.phone = bkCmd.getPhone();
//        // trip
//        this.departure = wayCmd.getDeparture();
//        this.departureAddress = wayCmd.getDepartureAddress();
//        this.departureTime = wayCmd.getDepartureTime();
//        this.destination = wayCmd.getDestination();
//        this.destinationAddress = wayCmd.getDestinationAddress();
//        this.destinationTime = wayCmd.getDestinationTime();
//    }

    public void returnTicket(UUID userId) {
        if(!this.userId.equals(userId)) {
            throw new ResponseException(BadRequestError.YOU_ARE_NOT_OWNER_OF_THIS);
        }
        if (!BookingStatus.PAYED.equals(this.status)) {
            throw new ResponseException(BadRequestError.UNABLE_TO_CHANGE_BOOKING_STATUS);
        }
        if (LocalDateTime.now().plusHours(24).isAfter(this.departureDate.atTime(this.departureTime))) {
            throw new ResponseException(BadRequestError.UNABLE_TO_RETURN_TICKET);
        }
        this.status = BookingStatus.RETURNED;
    }

    public void hardReturnTicket(){
        this.status = BookingStatus.FAILED;
    }

    public void payTicket() {
        if (!BookingStatus.WAIT_TO_PAY.equals(this.status)) {
            throw new ResponseException(BadRequestError.UNABLE_TO_CHANGE_BOOKING_STATUS);
        }
        this.status = BookingStatus.PAYED;
    }

    private void waitTicket() {
        if (!BookingStatus.OUT_OF_PAY.equals(this.status)) {
            throw new ResponseException(BadRequestError.UNABLE_TO_CHANGE_BOOKING_STATUS);
        }
        this.status = BookingStatus.WAIT_TO_PAY;
    }

    private void outPayTicket() {
        if (!BookingStatus.WAIT_TO_PAY.equals(this.status)) {
            throw new ResponseException(BadRequestError.UNABLE_TO_CHANGE_BOOKING_STATUS);
        }
        this.status = BookingStatus.OUT_OF_PAY;
    }

    private List<Ticket> createTicket(List<String> seatNumber) {
        if (CollectionUtils.isEmpty(seatNumber)) {
            throw new ResponseException(BadRequestError.SEATS_REQUIRED);
        }
        return seatNumber.stream().map(item -> new Ticket(this.id, item)).toList();
    }

    public static BookingWay enrich(BookingWay bookingWay) {
        if (isWayToPay(bookingWay)) bookingWay.waitTicket();
        return bookingWay;
    }

    public static BookingWay makePoor(BookingWay bookingWay) {
        if(BookingStatus.WAIT_TO_PAY.equals(bookingWay.getStatus())) {
            bookingWay.outPayTicket();
        }
        return bookingWay;
    }

    public static boolean isWayToPay(BookingWay bookingWay) {
        return BookingStatus.OUT_OF_PAY.equals(bookingWay.getStatus())
                && LocalDateTime.now().minusMinutes(20).atZone(ZoneId.systemDefault()).toInstant()
                .isBefore(bookingWay.getCreatedAt());
    }

    public static Long calculatePrice(BookingWay bookingWay) {
        if (Objects.isNull(bookingWay)
                || CollectionUtils.isEmpty(bookingWay.getTickets())
                || Objects.isNull(bookingWay.getPricePerSeat())) {
            return 0L;
        }
        return bookingWay.getPricePerSeat() * bookingWay.getTickets().size();
    }

    public static int calculateNumOfTickets(BookingWay bookingWay) {
        if (Objects.isNull(bookingWay)
                || CollectionUtils.isEmpty(bookingWay.getTickets())) {
            return 0;
        }
        return bookingWay.getTickets().size();
    }

    public void enrichTickets(List<Ticket> tickets) {
        this.tickets = new ArrayList<>();
        if (!CollectionUtils.isEmpty(tickets)) {
            this.tickets.addAll(tickets);
        }
    }
}
