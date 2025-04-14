package com.fasfood.tripservice.application.dto.response;

import com.fasfood.common.dto.AuditableDTO;
import com.fasfood.common.dto.response.BusTypeDTO;
import com.fasfood.common.enums.BusTypeEnum;
import com.fasfood.tripservice.infrastructure.support.enums.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TripDetailsDTO extends AuditableDTO {
    private UUID id;
    private UUID tripId;
    private Instant fromAt;
    private Instant toAt;
    private UUID driverId;
    private UUID assistantId;
    private BusTypeEnum type;
    private BusTypeDTO typeDetails;
    private UUID busId;
    private BusDTO bus;
    private long price;
    private TripStatus status;
}
