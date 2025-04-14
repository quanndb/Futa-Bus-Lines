package com.fasfood.tripservice.application.dto.response;

import com.fasfood.common.dto.AuditableDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TripDTO extends AuditableDTO {
    private UUID id;
    private String code;
    private List<TripTransitDTO> tripTransits;
}
