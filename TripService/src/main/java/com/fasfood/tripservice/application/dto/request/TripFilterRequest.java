package com.fasfood.tripservice.application.dto.request;

import com.fasfood.common.enums.BusTypeEnum;
import com.fasfood.tripservice.infrastructure.support.enums.TripOrderBy;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class TripFilterRequest {
    @NotNull(message = "DEPARTURE_REQUIRED")
    private UUID departureId;
    @NotNull(message = "DESTINATION_REQUIRED")
    private UUID destinationId;
    @NotNull(message = "DEPARTURE_DATE_REQUIRED")
    private LocalDate departureDate;
    // optional
    private List<UUID> detailsIds;
    private FormTimeToTimeRequest departureTime;
    private List<BusTypeEnum> busType;
    private List<TripOrderBy> orderBy;
}
