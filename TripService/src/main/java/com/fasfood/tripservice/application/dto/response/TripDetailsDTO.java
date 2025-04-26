package com.fasfood.tripservice.application.dto.response;

import com.fasfood.common.dto.AuditableDTO;
import com.fasfood.common.dto.response.BusTypeDTO;
import com.fasfood.common.enums.BusTypeEnum;
import com.fasfood.tripservice.infrastructure.support.enums.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripDetailsDTO extends AuditableDTO {
    private UUID id;
    private UUID tripId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private BusTypeEnum type;
    private BusTypeDTO typeDetails;
    private long price;
    private TripStatus status;
}
