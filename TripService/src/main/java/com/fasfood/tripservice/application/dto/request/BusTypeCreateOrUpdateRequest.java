package com.fasfood.tripservice.application.dto.request;

import com.fasfood.common.dto.request.Request;
import com.fasfood.common.enums.BusTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BusTypeCreateOrUpdateRequest extends Request {
    private BusTypeEnum type;
    private List<String> firstFloorSeats;
    private List<String> secondFloorSeats;
}
