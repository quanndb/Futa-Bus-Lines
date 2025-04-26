package com.fasfood.bookingservice.infrastructure.support.util;

import com.fasfood.bookingservice.domain.Booking;
import com.fasfood.web.support.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookingProducer {
    public static final String BOOKING_TOPIC = "bookings";
    public static final String BOOKING_GROUP = "booking-group";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonMapper jsonMapper;

    public void sendBookingMessage(Booking booking) throws JsonProcessingException {
        log.info("Sent message to topic {}", BOOKING_TOPIC);
        this.kafkaTemplate.send(BOOKING_TOPIC, this.jsonMapper.toJson(booking));
    }
}
