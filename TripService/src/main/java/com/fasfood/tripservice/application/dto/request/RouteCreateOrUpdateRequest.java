package com.fasfood.tripservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RouteCreateOrUpdateRequest extends Request {
    @NotNull(message = "DEPARTURE_CODE_REQUIRED")
    private UUID departureId;
    @NotNull(message = "DESTINATION_CODE_REQUIRED")
    private UUID destinationId;
    @NotNull(message = "DISTANCE_REQUIRED")
    private Double distance;
    @NotNull(message = "DURATION_REQUIRED")
    private Integer duration;
}
