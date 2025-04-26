package com.fasfood.iamservice.domain.query;

import com.fasfood.common.query.PagingQuery;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RolePagingQuery extends PagingQuery {
    private List<Boolean> isRoots;
}
