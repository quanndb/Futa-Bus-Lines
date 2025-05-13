package com.fasfood.tripservice.application.dto.request;

import com.fasfood.common.enums.BusTypeEnum;
import com.fasfood.tripservice.infrastructure.support.enums.TripStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripDetailsUpdateRequest {
    @NotNull(message = "BUS_TYPE_REQUIRED")
    private BusTypeEnum type;
    @NotNull(message = "PRICE_REQUIRED")
    private long price;
    @NotNull(message = "STATUS_REQUIRED")
    private TripStatus status;
}
