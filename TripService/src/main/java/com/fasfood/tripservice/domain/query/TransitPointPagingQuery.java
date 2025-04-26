package com.fasfood.tripservice.domain.query;

import com.fasfood.common.query.PagingQuery;
import com.fasfood.tripservice.infrastructure.support.enums.TransitType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TransitPointPagingQuery extends PagingQuery {
    private List<TransitType> types;
}
