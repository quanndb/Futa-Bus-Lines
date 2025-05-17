package com.fasfood.bookingservice.infrastructure.support.util;

import com.fasfood.bookingservice.infrastructure.support.exception.BadRequestError;
import com.fasfood.common.dto.response.TripDetailsResponse;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.client.support.Cacher;
import com.fasfood.client.support.DataCacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TripCache implements Cacher<TripDetailsResponse, TripDetailsResponse> {

    public static final String TRIP_KEY = "TRIP";
    private final DataCacher dataCacher;

    @Override
    public void put(TripDetailsResponse key, TripDetailsResponse value, Duration duration) {
        LocalDateTime departureTime = value.getDepartureDate().atTime(value.getDepartureTime());
        if (departureTime.isBefore(LocalDateTime.now())) {
            throw new ResponseException(BadRequestError.INVALID_TRIP_DETAILS);
        }
        this.dataCacher.put(getTripKey(key), value, Duration.between(LocalDateTime.now(), departureTime));
    }

    @Override
    public TripDetailsResponse get(TripDetailsResponse key) {
        return this.dataCacher.get(getTripKey(key), TripDetailsResponse.class);
    }

    @Override
    public void remove(TripDetailsResponse key) {
        this.dataCacher.remove(getTripKey(key));
    }

    @Override
    public boolean hasKey(TripDetailsResponse key) {
        return this.dataCacher.hasKey(getTripKey(key));
    }

    public static String getTripKey(TripDetailsResponse trip) {
        return String.join(":", TRIP_KEY, trip.getTripDetailsId().toString(),
                trip.getDepartureId().toString(), trip.getDestinationId().toString(),
                trip.getDepartureDate().toString());
    }
}
