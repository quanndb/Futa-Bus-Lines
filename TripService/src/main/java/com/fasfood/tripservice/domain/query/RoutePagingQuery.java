package com.fasfood.tripservice.domain.query;

import com.fasfood.common.query.PagingQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class RoutePagingQuery extends PagingQuery {
    private String departureKeyword;
    private String destinationKeyword;
}
