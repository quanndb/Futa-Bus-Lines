package com.fasfood.tripservice.domain.cmd;

import com.fasfood.common.enums.BusTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BusTypeCreateOrUpdateCmd {
    private BusTypeEnum type;
    private List<String> firstFloorSeats;
    private List<String> secondFloorSeats;
}
