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
import com.fasfood.bookingservice.infrastructure.persistence.repository.projection.BookedProjection;
import com.fasfood.bookingservice.infrastructure.support.exception.BadRequestError;
import com.fasfood.bookingservice.infrastructure.support.exception.NotFoundError;
import com.fasfood.bookingservice.infrastructure.support.util.BookingProducer;
import com.fasfood.bookingservice.infrastructure.support.util.BookingTripCache;
import com.fasfood.bookingservice.infrastructure.support.util.TripCache;
import com.fasfood.client.client.notification.NotificationClient;
import com.fasfood.client.client.payment.PaymentClient;
import com.fasfood.client.client.trip.TripClient;
import com.fasfood.client.config.security.ClientAuthentication;
import com.fasfood.common.constant.EmailTitleReadModel;
import com.fasfood.common.dto.request.ClientRequest;
import com.fasfood.common.dto.request.GetBookedRequest;
import com.fasfood.common.dto.request.PayRequest;
import com.fasfood.common.dto.request.SendEmailRequest;
import com.fasfood.common.dto.response.PaymentLinkResponse;
import com.fasfood.common.dto.response.TripDetailsResponse;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.web.support.SecurityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties({ClientRequest.class})
public class BookingCmdServiceImpl implements BookingCmdService {

    private static final String NOT_NULL_SEAT = "Seat cannot be null";
    private static final String TRIP_NOT_CONTAINS_SEAT = "Trip does not contain seat: {0}";
    private static final String BOOKED_SEAT = "Booked seat: {0}";

    @Value("${app.client.return-url}")
    private String RETURN_URL;
    private final BookingRepository bookingRepository;
    private final BookingEntityRepository bookingEntityRepository;
    private final BookingCmdMapper bookingCmdMapper;
    private final BookingDTOMapper dtoMapper;
    private final TripClient tripClient;
    private final BookingProducer kafkaProducer;
    private final PaymentClient paymentClient;
    private final NotificationClient notificationClient;
    private final BookingTripCache bookingTripCache;
    private final TripCache tripCache;
    private final ClientAuthentication clientAuthentication;
    private final ClientRequest clientRequest;

    @Override
    @Transactional
    public BookingDTO send(BookingRequest request) throws JsonProcessingException {
        BookingCreateCmd cmd = this.bookingCmdMapper
                .from(this.getTripDetailsResponse(request.getDepartureTrip()),
                        this.getTripDetailsResponse(request.getReturnTrip()), request);
        Booking newBooking =  new Booking(SecurityUtils.getUserId(), cmd);
        BookingDTO dto = this.dtoMapper.from(newBooking);
        this.kafkaProducer.sendBookingMessage(newBooking);
        return dto;
    }

    @Override
    @Transactional
    public BookingDTO consumeBooking(Booking booking) {
        return this.dtoMapper.from(this.bookingRepository.save(booking));
    }

    @Override
    public void payedBooking(String code) throws JsonProcessingException {
        Booking booking = this.bookingRepository.getById(code);
        this.kafkaProducer.sendPayedBookingMessage(booking);
    }

    @Transactional
    public void consumePayedBooking(Booking booking) {
        try {
            booking.payTicket();
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
            this.paymentClient.returnPayment(booking.getCode(), String.join(" ", "Bearer", this.clientAuthentication.getClientToken(this.clientRequest).getAccessToken()));
            this.bookingRepository.save(booking.hardReturn());
            return;
        }
        this.bookingRepository.save(booking);
        try {
            this.notificationClient.send(this.from(booking), String.join(" ", "Bearer", this.clientAuthentication.getClientToken(this.clientRequest).getAccessToken()));
            this.bookingTripCache.remove(new GetBookedRequest(List.of(booking.getDepartureTrip().getTripDetailsId()),
                    booking.getDepartureTrip().getDepartureDate()));
            if (Objects.nonNull(booking.getReturnTrip())) {
                this.bookingTripCache.remove(new GetBookedRequest(List.of(booking.getReturnTrip().getTripDetailsId()),
                        booking.getReturnTrip().getDepartureDate()));
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    @Transactional
    public BookingDTO returnBooking(String code) {
        Booking booking = this.bookingRepository.getById(code);
        this.bookingTripCache.remove(new GetBookedRequest(List.of(booking.getDepartureTrip().getTripDetailsId()),
                booking.getDepartureTrip().getDepartureDate()));
        if (Objects.nonNull(booking.getReturnTrip())) {
            this.bookingTripCache.remove(new GetBookedRequest(List.of(booking.getReturnTrip().getTripDetailsId()),
                    booking.getReturnTrip().getDepartureDate()));
        }
        this.bookingRepository.save(booking.returnTicket(SecurityUtils.getUserId()));
        this.paymentClient.returnPayment(booking.getCode());
        return this.dtoMapper.from(booking);
    }

    @Override
    @Transactional
    public BookingDTO createPaymentLink(String code, boolean isUseWallet) {
        Booking found = this.bookingRepository.getById(code);
        if (Objects.nonNull(found.getPaymentLink())) {
            throw new ResponseException(BadRequestError.EXISTED_PAYMENT, code);
        }
        found.createPaymentLink("");
        PayRequest payRequest = PayRequest.builder()
                .userId(SecurityUtils.getUserId())
                .amount(found.getTotalPrice())
                .code(found.getCode())
                .isUseWallet(isUseWallet)
                .build();
        PaymentLinkResponse response = this.paymentClient
                .createPayment(payRequest, String.join(" ", "Bearer", this.clientAuthentication.getClientToken(this.clientRequest).getAccessToken())).getData();
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
        TripDetailsResponse response = TripDetailsResponse.builder()
                .tripDetailsId(tripDetailsId)
                .departureId(departureId)
                .destinationId(destinationId)
                .departureDate(departureDate)
                .build();
        if (this.tripCache.hasKey(response)) {
            response = this.tripCache.get(response);
        } else {
            response = this.tripClient
                    .getDetails(tripDetailsId, departureId, destinationId, departureDate.toString()).getData();
            if (Objects.isNull(response)) {
                throw new ResponseException(NotFoundError.TRIP_NOT_FOUND);
            }
            this.tripCache.put(response, response);
        }
        TripDetailsResponse finalResponse = response;
        List<String> bookedSeats = this.bookingEntityRepository
                .getBooked(new GetBookedRequest(List.of(tripDetailsId), departureDate))
                .stream().map(BookedProjection::getSeatNumber).toList();
        seats.forEach(seat -> {
            if (Objects.isNull(seat)) {
                errors.add(NOT_NULL_SEAT);
            } else {
                if (!finalResponse.getSeats().contains(seat)) {
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
        variables.put("qrCodeUrl", String.join("", this.RETURN_URL, "/bookings/", booking.getCode()));
        variables.put("code", booking.getCode());
        variables.put("dpRoute", booking.getDepartureRoute());
        variables.put("dpBusType", booking.getDepartureTrip().getBusType());
        variables.put("dpQuantity", booking.getDepartureTrip().getTickets().size());
        variables.put("dpSeats", booking.getDepartureTrip().getTickets().stream().map(Ticket::getSeatNumber).toList());
        variables.put("dpDeparture", booking.getDepartureTrip().getDepartureAddress());
        variables.put("dpTime", booking.getDepartureTime());
        variables.put("dpDestination", booking.getDepartureTrip().getDestinationAddress());
        variables.put("recipientName", booking.getDepartureTrip().getEmail());
        if (Objects.nonNull(booking.getReturnTrip())) {
            template = "TP-M9WT2PKUS5G";
            variables.put("dsRoute", booking.getReturnRoute());
            variables.put("dsBusType", booking.getReturnTrip().getBusType());
            variables.put("dsQuantity", booking.getReturnTrip().getTickets().size());
            variables.put("dsSeats", booking.getReturnTrip().getTickets().stream().map(Ticket::getSeatNumber).toList());
            variables.put("dsDeparture", booking.getReturnTrip().getDepartureAddress());
            variables.put("dsTime", booking.getReturnTime());
            variables.put("dsDestination", booking.getReturnTrip().getDestinationAddress());
        }
        return SendEmailRequest.builder()
                .templateCode(template)
                .subject(EmailTitleReadModel.ORDER)
                .to(List.of(booking.getDepartureTrip().getEmail()))
                .variables(variables)
                .build();
    }
}
