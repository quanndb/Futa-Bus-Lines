package com.fasfood.client.client.trip;

import com.fasfood.client.config.FeignClientConfiguration;
import com.fasfood.common.dto.response.BusTypeDTO;
import com.fasfood.common.dto.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(
        name = "trip",
        contextId = "trip",
        configuration = {FeignClientConfiguration.class},
        fallbackFactory = TripClientFallback.class
)
public interface TripClient {
    @GetMapping(value = "/api/v1/bus-types")
    Response<List<BusTypeDTO>> getAll();
}
