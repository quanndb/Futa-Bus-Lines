package com.fasfood.tripservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import com.fasfood.common.enums.BusTypeEnum;
import com.fasfood.tripservice.infrastructure.support.enums.TripStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class TripDetailsCreateOrUpdateRequest extends Request {
    private String tripCode;
    @NotNull(message = "FROM_AT_REQUIRED")
    private Instant fromAt;
    @NotNull(message = "TO_AT_REQUIRED")
    private Instant toAt;
    private UUID driverId;
    private UUID assistantId;
    @NotNull(message = "BUS_TYPE_REQUIRED")
    private BusTypeEnum type;
    private UUID busId;
    @NotNull(message = "PRICE_REQUIRED")
    private long price;
    @NotNull(message = "STATUS_REQUIRED")
    private TripStatus status;
}
