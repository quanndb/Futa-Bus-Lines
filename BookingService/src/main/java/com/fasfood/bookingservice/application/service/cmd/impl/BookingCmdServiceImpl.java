package com.fasfood.bookingservice.application.service.cmd.impl;

import com.fasfood.bookingservice.application.dto.mapper.BookingDTOMapper;
import com.fasfood.bookingservice.application.dto.request.BookingRequest;
import com.fasfood.bookingservice.application.dto.request.BookingWayRequest;
import com.fasfood.bookingservice.application.dto.response.BookingDTO;
import com.fasfood.bookingservice.application.mapper.BookingCmdMapper;
import com.fasfood.bookingservice.application.service.cmd.BookingCmdService;
import com.fasfood.bookingservice.domain.Booking;
import com.fasfood.bookingservice.domain.BookingWay;
import com.fasfood.bookingservice.domain.Ticket;
import com.fasfood.bookingservice.domain.cmd.BookingCreateCmd;
import com.fasfood.bookingservice.domain.repository.BookingRepository;
import com.fasfood.bookingservice.infrastructure.persistence.repository.BookingEntityRepository;
import com.fasfood.bookingservice.infrastructure.support.exception.BadRequestError;
import com.fasfood.bookingservice.infrastructure.support.util.BookingProducer;
import com.fasfood.client.client.notification.NotificationClient;
import com.fasfood.client.client.payment.PaymentClient;
import com.fasfood.client.client.trip.TripClient;
import com.fasfood.common.constant.EmailTitleReadModel;
import com.fasfood.common.dto.request.PayRequest;
import com.fasfood.common.dto.request.SendEmailRequest;
import com.fasfood.common.dto.response.PaymentLinkResponse;
import com.fasfood.common.dto.response.TripDetailsResponse;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.web.support.SecurityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingCmdServiceImpl implements BookingCmdService {

    private static final String NOT_NULL_SEAT = "Seat cannot be null";
    private static final String TRIP_NOT_CONTAINS_SEAT = "Trip does not contain seat: {0}";
    private static final String BOOKED_SEAT = "Booked seat: {0}";

    private final BookingRepository bookingRepository;
    private final BookingEntityRepository bookingEntityRepository;
    private final BookingCmdMapper bookingCmdMapper;
    private final BookingDTOMapper dtoMapper;
    private final TripClient tripClient;
    private final BookingProducer kafkaProducer;
    private final PaymentClient paymentClient;
    private final NotificationClient notificationClient;

    @Override
    @Transactional
    public void send(BookingRequest request) throws JsonProcessingException {
        BookingCreateCmd cmd = this.bookingCmdMapper
                .from(this.getTripDetailsResponse(request.getDepartureTrip()),
                        this.getTripDetailsResponse(request.getReturnTrip()), request);
        this.kafkaProducer.sendBookingMessage(new Booking(SecurityUtils.getUserId(), cmd));
    }

    @Override
    @Transactional
    public BookingDTO consumeBooking(Booking booking) {
        return this.dtoMapper.from(this.bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public void payBooking(String code) throws JsonProcessingException {
        Booking booking = this.bookingRepository.getById(code);
        booking.payTicket();
        try {
            BookingWay departure = booking.getDepartureTrip();
            BookingWay returnTrip = booking.getReturnTrip();
            this.checkValidBooking(departure.getTripDetailsId(), departure.getDepartureId(),
                    departure.getDestinationId(), departure.getDepartureDate(),
                    departure.getTickets().stream().map(Ticket::getSeatNumber).toList());
            if (Objects.nonNull(returnTrip)) {
                this.checkValidBooking(returnTrip.getTripDetailsId(), returnTrip.getDepartureId(),
                        returnTrip.getDestinationId(), returnTrip.getDepartureDate(),
                        returnTrip.getTickets().stream().map(Ticket::getSeatNumber).toList());
            }
        } catch (ResponseException e) {
            this.paymentClient.returnPayment(code);
            this.bookingRepository.save(booking.hardReturn());
        }
        this.notificationClient.send(this.from(booking));
        this.bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public BookingDTO returnBooking(String code) {
        Booking found = this.bookingRepository.getById(code);
        this.paymentClient.returnPayment(found.getCode());
        this.bookingRepository.save(found.returnTicket(SecurityUtils.getUserId()));
        return this.dtoMapper.from(found);
    }

    @Override
    @Transactional
    public BookingDTO createPaymentLink(String code, boolean isUseWallet) {
        Booking found = this.bookingRepository.getById(code);
        if (Objects.nonNull(found.getPaymentLink())) {
            throw new ResponseException(BadRequestError.EXISTED_PAYMENT, code);
        }
        PaymentLinkResponse response = this.paymentClient.createPayment(PayRequest.builder()
                .userId(SecurityUtils.getUserId())
                .amount(found.getTotalPrice())
                .code(found.getCode())
                .isUseWallet(isUseWallet)
                .build()).getData();
        return this.dtoMapper.from(this.bookingRepository.save(found.createPaymentLink(response.getPaymentLink())));
    }

    private TripDetailsResponse getTripDetailsResponse(BookingWayRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        return this.checkValidBooking(request.getTripDetailsId(), request.getDepartureId(), request.getDestinationId(), request.getDepartureDate(), request.getSeats());
    }

    private TripDetailsResponse checkValidBooking(UUID tripDetailsId, UUID departureId, UUID destinationId, LocalDate departureDate, List<String> seats) {
        List<String> errors = new ArrayList<>();
        TripDetailsResponse response = this.tripClient
                .getDetails(tripDetailsId, departureId, destinationId, departureDate.toString()).getData();
        List<String> bookedSeats = this.bookingEntityRepository
                .getBookedSeats(tripDetailsId, departureDate);
        seats.forEach(seat -> {
            if (Objects.isNull(seat)) {
                errors.add(NOT_NULL_SEAT);
            } else {
                if (!response.getSeats().contains(seat)) {
                    errors.add(MessageFormat.format(TRIP_NOT_CONTAINS_SEAT, seat));
                }
                if (bookedSeats.contains(seat)) {
                    errors.add(MessageFormat.format(BOOKED_SEAT, seat));
                }
            }
        });
        if (!CollectionUtils.isEmpty(errors)) {
            throw new ResponseException(BadRequestError.INVALID_SEATS, errors);
        }
        return response;
    }

    private SendEmailRequest from(Booking booking) {
        Map<String, Object> variables = new HashMap<>();
        String template = "TP-M9WT2NOW69C";
        variables.put("code", booking.getCode());
        variables.put("dpRoute", booking.getDepartureRoute());
        variables.put("dpBusType", "type");
        variables.put("dpQuantity", booking.getDepartureTrip().getTickets().size());
        variables.put("dpSeats", booking.getDepartureTrip().getTickets().stream().map(Ticket::getSeatNumber).toList());
        variables.put("dpDeparture", booking.getDepartureTrip().getDepartureAddress());
        variables.put("dpTime", booking.getDepartureTime());
        variables.put("dpDestination", booking.getDepartureTrip().getDestinationAddress());
        variables.put("recipientName", booking.getDepartureTrip().getCreatedBy());
        if (Objects.nonNull(booking.getReturnTrip())) {
            template = "TP-M9WT2PKUS5G";
            variables.put("dsRoute", booking.getReturnRoute());
            variables.put("dsBusType", "type");
            variables.put("dsQuantity", booking.getReturnTrip().getTickets().size());
            variables.put("dsSeats", booking.getReturnTrip().getTickets().stream().map(Ticket::getSeatNumber).toList());
            variables.put("dsDeparture", booking.getReturnTrip().getDepartureAddress());
            variables.put("dsTime", booking.getReturnTime());
            variables.put("dsDestination", booking.getReturnTrip().getDestinationAddress());
        }
        return SendEmailRequest.builder()
                .templateCode(template)
                .subject(EmailTitleReadModel.ORDER)
                .to(List.of(booking.getDepartureTrip().getCreatedBy()))
                .variables(variables)
                .build();
    }
}
