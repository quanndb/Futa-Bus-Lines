package com.fasfood.client.client.trip;

import com.fasfood.client.config.FeignClientConfiguration;
import com.fasfood.common.dto.response.BusTypeDTO;
import com.fasfood.common.dto.response.Response;
import com.fasfood.common.dto.response.TripDetailsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(
        url = "${app.clients.trip:}",
        name = "trip",
        contextId = "trip",
        configuration = {FeignClientConfiguration.class},
        fallbackFactory = TripClientFallback.class
)
public interface TripClient {
    @GetMapping(value = "/api/v1/bus-types/{id}")
    Response<BusTypeDTO> getById(@PathVariable UUID id);

    @GetMapping(value = "/api/v1/trips-details/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    Response<TripDetailsResponse> getDetails(@PathVariable UUID id, @RequestParam UUID departureId,
                                             @RequestParam UUID arrivalId, @RequestParam String departureDate);
}
