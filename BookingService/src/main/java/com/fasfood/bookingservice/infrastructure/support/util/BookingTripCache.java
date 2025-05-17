package com.fasfood.bookingservice.infrastructure.support.util;

import com.fasfood.bookingservice.application.dto.response.BookedResponse;
import com.fasfood.common.dto.request.GetBookedRequest;
import com.fasfood.client.support.Cacher;
import com.fasfood.client.support.DataCacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class BookingTripCache implements Cacher<GetBookedRequest, BookedResponse> {

    public static final String BOOKED_KEY = "BOOKED";
    private final DataCacher dataCacher;

    @Override
    public void put(GetBookedRequest key, BookedResponse value, Duration duration) {
        LocalDateTime tomorrow = key.getStartDate().plusDays(1).atStartOfDay();
        this.dataCacher.put(getKey(key), value, Duration.between(key.getStartDate().atTime(LocalTime.now()), tomorrow));
    }

    @Override
    public BookedResponse get(GetBookedRequest key) {
        return this.dataCacher.get(getKey(key), BookedResponse.class);
    }

    @Override
    public void remove(GetBookedRequest key) {
        this.dataCacher.remove(getKey(key));
    }

    @Override
    public boolean hasKey(GetBookedRequest key) {
        return this.dataCacher.hasKey(getKey(key));
    }

    private static String getKey(GetBookedRequest key) {
        return String.join(":", BOOKED_KEY, key.getDetailsIds().getFirst().toString(), key.getStartDate().toString());
    }
}
