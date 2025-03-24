package com.fasfood.common.query;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
public class PagingQuery extends Query {
    public static final String ASC_SYMBOL = "ASC";
    public static final String DESC_SYMBOL = "DESC";
    protected int pageIndex = 1;
    protected int pageSize = 30;
    protected String sortBy;
    protected List<UUID> ids;
    private String keyword;
}
