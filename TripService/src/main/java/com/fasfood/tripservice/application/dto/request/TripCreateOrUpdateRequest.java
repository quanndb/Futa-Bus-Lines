package com.fasfood.tripservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TripCreateOrUpdateRequest extends Request {
    @NotBlank(message = "TRIP_CODE_REQUIRED")
    private String code;
    @NotBlank(message = "TRIP_NAME_REQUIRED")
    private String name;
    private String description;
    private List<TripTransitCreateOrUpdateRequest> transits;
}
