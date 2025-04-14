package com.fasfood.tripservice.domain.query;

import com.fasfood.common.query.PagingQuery;
import com.fasfood.tripservice.infrastructure.support.enums.BusStatus;
import com.fasfood.common.enums.BusTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class BusPagingQuery extends PagingQuery {
    private List<BusTypeEnum> types;
    private List<String> licensePlates;
    private List<BusStatus> statuses;
}
