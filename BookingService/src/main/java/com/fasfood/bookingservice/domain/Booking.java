package com.fasfood.bookingservice.domain;

import com.fasfood.bookingservice.domain.cmd.BookingCreateCmd;
import com.fasfood.bookingservice.infrastructure.support.enums.BookingStatus;
import com.fasfood.bookingservice.infrastructure.support.enums.BookingType;
import com.fasfood.common.constant.BookingReadModel;
import com.fasfood.common.domain.Domain;
import com.fasfood.util.RandomCodeUtil;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
@Getter
public class Booking extends Domain {
    private BookingWay departureTrip;
    private BookingWay returnTrip;
    private String code;
    private BookingStatus status;
    private String paymentLink;
    private String departureRoute;
    private String returnRoute;
    private LocalDateTime departureTime;
    private LocalDateTime returnTime;
    private int numOfTickets;
    private Long totalPrice;

    public Booking(UUID userId, BookingCreateCmd cmd) {
        this.code = RandomCodeUtil.generateOrderCode(BookingReadModel.BOOKING_PREFIX);
        this.departureTrip = new BookingWay(userId, this.code, BookingType.DEPARTURE, cmd, cmd.getDepartureTrip());
        if (Objects.nonNull(cmd.getReturnTrip())) {
            this.returnTrip = new BookingWay(userId, this.code, BookingType.RETURN, cmd, cmd.getReturnTrip());
            this.returnRoute = this.returnTrip.getRoute();
            this.returnTime = this.returnTrip.getDepartureDate().atTime(this.returnTrip.getDepartureTime());
        }
        this.status = this.departureTrip.getStatus();
        this.departureRoute = this.departureTrip.getRoute();
        this.departureTime = this.departureTrip.getDepartureDate().atTime(this.departureTrip.getDepartureTime());
        this.totalPrice = BookingWay.calculatePrice(this.departureTrip) + BookingWay.calculatePrice(this.returnTrip);
        this.numOfTickets = BookingWay.calculateNumOfTickets(this.departureTrip) + BookingWay.calculateNumOfTickets(this.returnTrip);
        this.createdAt = this.departureTrip.getCreatedAt();
    }

//    public Booking update(BookingUpdateCmd cmd) {
//        this.departureTrip.update(cmd, cmd.getDepartureTrip());
//        if (Objects.nonNull(cmd.getReturnTrip())) {
//            this.returnTrip.update(cmd, cmd.getReturnTrip());
//        }
//        return this;
//    }

    public Booking returnTicket(UUID userId) {
        this.departureTrip.returnTicket(userId);
        if (Objects.nonNull(this.returnTrip)) {
            this.returnTrip.returnTicket(userId);
        }
        return this;
    }

    public void payTicket() {
        this.departureTrip.payTicket();
        if (Objects.nonNull(this.returnTrip)) {
            this.returnTrip.payTicket();
        }
    }

    public Booking hardReturn() {
        this.departureTrip.hardReturnTicket();
        if (Objects.nonNull(this.returnTrip)) {
            this.returnTrip.hardReturnTicket();
        }
        return this;
    }

    public Booking createPaymentLink(String paymentLink) {
        this.departureTrip.createPaymentLink(paymentLink);
        if (Objects.nonNull(this.returnTrip)) {
            this.returnTrip.createPaymentLink(paymentLink);
        }
        this.paymentLink = paymentLink;
        return this;
    }

    public Booking enrich(BookingWay departureTrip, BookingWay returnTrip) {
        this.code = departureTrip.getCode();
        this.paymentLink = departureTrip.getPaymentLink();
        this.departureTrip = BookingWay.enrich(departureTrip);
        this.departureRoute = departureTrip.getRoute();
        this.departureTime = this.departureTrip.getDepartureDate().atTime(this.departureTrip.getDepartureTime());
        this.status = departureTrip.getStatus();
        if (Objects.nonNull(returnTrip)) {
            this.returnTrip = BookingWay.enrich(returnTrip);
            this.returnRoute = this.returnTrip.getRoute();
            this.returnTime = this.returnTrip.getDepartureDate().atTime(this.returnTrip.getDepartureTime());
        }
        this.totalPrice = BookingWay.calculatePrice(this.departureTrip) + BookingWay.calculatePrice(this.returnTrip);
        this.numOfTickets = BookingWay.calculateNumOfTickets(this.departureTrip) + BookingWay.calculateNumOfTickets(this.returnTrip);
        this.createdAt = this.departureTrip.getCreatedAt();
        return this;
    }
}
