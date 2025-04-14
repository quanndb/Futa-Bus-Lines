package com.fasfood.tripservice.infrastructure.persistence.repository.impl;

import com.fasfood.common.dto.response.StatisticResponse;
import com.fasfood.persistence.custom.AbstractPagingEntityRepository;
import com.fasfood.tripservice.domain.query.TransitPointPagingQuery;
import com.fasfood.tripservice.infrastructure.persistence.entity.TransitPointEntity;
import com.fasfood.util.QueryBuilder;

import java.util.List;

public class TransitPointEntityRepositoryImpl extends AbstractPagingEntityRepository<TransitPointEntity, TransitPointPagingQuery, StatisticResponse> {
    protected TransitPointEntityRepositoryImpl() {
        super(TransitPointEntity.class);
    }

    @Override
    protected void createWhereClause(QueryBuilder queryBuilder, TransitPointPagingQuery query) {
        queryBuilder.like(List.of("id", "name", "address", "hotline"), query.getKeyword())
                .whereIn("type", query.getTypes())
                .where("deleted", false);
    }
}
