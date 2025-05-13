package com.fasfood.client.client.booking;

import com.fasfood.client.config.FeignClientConfiguration;
import com.fasfood.common.dto.request.GetBookedRequest;
import com.fasfood.common.dto.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(
        url = "${app.clients.booking:}",
        name = "booking",
        contextId = "booking",
        configuration = {FeignClientConfiguration.class},
        fallbackFactory = BookingClientFallback.class
)
public interface BookingClient {
    @GetMapping(value = "/api/v1/booking-seats")
    Response<Map<UUID, List<String>>> getBooked(@SpringQueryMap GetBookedRequest request);

    @PostMapping(value = "/api/v1/bookings/payed")
    Response<Void> payBooking(@RequestParam String code);

    @PostMapping(value = "/api/v1/bookings/payed", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Response<Void> payBooking(@RequestParam String code, @RequestHeader("Authorization") String authorization);
}
