package com.fasfood.tripservice.application.dto.response;

import com.fasfood.common.dto.AuditableDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class RouteDTO extends AuditableDTO {
    private UUID id;
    private PlaceDTO departure;
    @JsonIgnore
    private String departureCode;
    private PlaceDTO destination;
    @JsonIgnore
    private String destinationCode;
    private Double distance;
    private Integer duration;
}
