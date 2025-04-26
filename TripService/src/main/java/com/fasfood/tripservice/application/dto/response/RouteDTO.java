package com.fasfood.tripservice.application.dto.response;

import com.fasfood.common.dto.AuditableDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class RouteDTO extends AuditableDTO {
    private UUID id;
    private String departure;
    private UUID departureId;
    private String destination;
    private UUID destinationId;
    private Double distance;
    private Integer duration;
}
