package com.fasfood.tripservice.domain.cmd;

import com.fasfood.common.enums.BusTypeEnum;
import com.fasfood.tripservice.infrastructure.support.enums.TripStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TripDetailsCreateOrUpdateCmd {
    private String tripCode;
    private LocalDate fromDate;
    private LocalDate toDate;
    private BusTypeEnum type;
    private long price;
    private TripStatus status;
}
