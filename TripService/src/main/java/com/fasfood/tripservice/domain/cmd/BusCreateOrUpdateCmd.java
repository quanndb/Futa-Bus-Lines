package com.fasfood.tripservice.domain.cmd;

import com.fasfood.tripservice.infrastructure.support.enums.BusStatus;
import com.fasfood.common.enums.BusTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusCreateOrUpdateCmd {
    private String licensePlate;
    private BusTypeEnum type;
    private BusStatus status;
}
