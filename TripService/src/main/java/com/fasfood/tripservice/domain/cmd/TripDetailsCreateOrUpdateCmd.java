package com.fasfood.tripservice.domain.cmd;

import com.fasfood.common.enums.BusTypeEnum;
import com.fasfood.tripservice.infrastructure.support.enums.TripStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class TripDetailsCreateOrUpdateCmd {
    private String tripCode;
    private Instant fromAt;
    private Instant toAt;
    private UUID driverId;
    private UUID assistantId;
    private BusTypeEnum type;
    private UUID busId;
    private long price;
    private TripStatus status;
}
