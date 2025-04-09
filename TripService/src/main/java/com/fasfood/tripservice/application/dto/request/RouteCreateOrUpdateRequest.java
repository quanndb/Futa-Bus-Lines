package com.fasfood.tripservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteCreateOrUpdateRequest extends Request {
    @NotBlank(message = "DEPARTURE_CODE_REQUIRED")
    private String departureCode;
    @NotBlank(message = "DESTINATION_CODE_REQUIRED")
    private String destinationCode;
    @NotNull(message = "DISTANCE_REQUIRED")
    private Double distance;
    @NotNull(message = "DURATION_REQUIRED")
    private Integer duration;
}
