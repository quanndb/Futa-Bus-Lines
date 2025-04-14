package com.fasfood.tripservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import com.fasfood.tripservice.infrastructure.support.enums.TripTransitType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class TripTransitCreateOrUpdateRequest extends Request {
    private UUID transitPointId;
    private LocalTime arrivalTime;
    private TripTransitType type;
}
