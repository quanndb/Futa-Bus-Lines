package com.fasfood.tripservice.domain.query;

import com.fasfood.common.query.PagingQuery;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TripPagingQuery extends PagingQuery {
    private List<String> codes;
}
