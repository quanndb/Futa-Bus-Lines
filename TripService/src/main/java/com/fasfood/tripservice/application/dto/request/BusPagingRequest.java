package com.fasfood.tripservice.application.dto.request;

import com.fasfood.common.dto.request.PagingRequest;
import com.fasfood.tripservice.infrastructure.support.enums.BusStatus;
import com.fasfood.common.enums.BusTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BusPagingRequest extends PagingRequest {
    private List<BusTypeEnum> types;
    private List<String> licensePlates;
    private List<BusStatus> statuses;
}
