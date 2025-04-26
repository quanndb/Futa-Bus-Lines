package com.fasfood.bookingservice.infrastructure.support.util;

import com.fasfood.bookingservice.application.service.cmd.BookingCmdService;
import com.fasfood.bookingservice.domain.Booking;
import com.fasfood.web.support.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingConsumer {

    private final JsonMapper jsonMapper;
    private final BookingCmdService cmdService;

    @KafkaListener(topics = BookingProducer.BOOKING_TOPIC, groupId = BookingProducer.BOOKING_GROUP)
    public void listenBooking(String message) throws JsonProcessingException {
        Booking received = this.jsonMapper.fromJson(message, Booking.class);
        log.info("Saved booking: {}", this.cmdService.consumeBooking(received));
    }
}