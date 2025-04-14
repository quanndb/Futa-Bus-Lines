package com.fasfood.tripservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "TRANSITS_REQUIRED")
    private List<TripTransitCreateOrUpdateRequest> transits;
}
