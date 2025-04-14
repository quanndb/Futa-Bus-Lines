package com.fasfood.tripservice.application.dto.response;

import com.fasfood.common.dto.AuditableDTO;
import com.fasfood.tripservice.infrastructure.support.enums.BusStatus;
import com.fasfood.common.enums.BusTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BusDTO extends AuditableDTO {
    private UUID id;
    private String licensePlate;
    private BusStatus status;
    private BusTypeEnum type;
}
