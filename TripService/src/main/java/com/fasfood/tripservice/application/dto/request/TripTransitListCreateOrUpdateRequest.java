package com.fasfood.tripservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TripTransitListCreateOrUpdateRequest extends Request {
    @NotEmpty(message = "TRIP_TRANSITS_REQUIRED")
    private List<TripTransitCreateOrUpdateRequest> transits;
}
