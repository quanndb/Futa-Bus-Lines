package com.fasfood.tripservice.application.dto.response;

import com.fasfood.common.dto.AuditableDTO;
import com.fasfood.tripservice.infrastructure.support.enums.TripTransitType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripTransitDTO extends AuditableDTO {
    private UUID id;
    private UUID tripId;
    private UUID transitPointId;
    private TransitPointDTO transitPoint;
    private LocalTime arrivalTime;
    private int transitOrder;
    private TripTransitType type;
}
