package com.fasfood.client.client.booking;

import com.fasfood.client.config.FeignClientConfiguration;
import com.fasfood.common.dto.request.GetBookedRequest;
import com.fasfood.common.dto.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(
        name = "booking",
        contextId = "booking",
        configuration = {FeignClientConfiguration.class},
        fallbackFactory = BookingClientFallback.class
)
public interface BookingClient {
    @GetMapping(value = "/api/v1/bookings")
    Response<Map<UUID, List<String>>> getBooked(@SpringQueryMap GetBookedRequest request);
}
