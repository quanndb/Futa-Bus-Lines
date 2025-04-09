package com.fasfood.tripservice.domain.query;

import com.fasfood.common.query.PagingQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class RoutePagingQuery extends PagingQuery {
    private String departureKeyword;
    private String destinationKeyword;
    private List<String> departureCodes;
    private List<String> destinationCodes;
}
