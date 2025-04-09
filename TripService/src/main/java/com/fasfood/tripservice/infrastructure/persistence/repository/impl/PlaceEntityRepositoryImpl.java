package com.fasfood.tripservice.infrastructure.persistence.repository.impl;

import com.fasfood.common.dto.response.StatisticResponse;
import com.fasfood.persistence.custom.AbstractPagingEntityRepository;
import com.fasfood.tripservice.domain.query.PlacePagingQuery;
import com.fasfood.tripservice.infrastructure.persistence.entity.PlaceEntity;
import com.fasfood.util.QueryBuilder;

import java.util.List;

public class PlaceEntityRepositoryImpl extends AbstractPagingEntityRepository<PlaceEntity, PlacePagingQuery, StatisticResponse> {
    protected PlaceEntityRepositoryImpl() {
        super(PlaceEntity.class);
    }

    @Override
    protected void createWhereClause(QueryBuilder queryBuilder, PlacePagingQuery query) {
        queryBuilder.like(List.of("id","name","code","address"), query.getKeyword())
                .whereIn("id", query.getIds())
                .whereIn("code", query.getCodes())
                .where("deleted", false);
    }
}
