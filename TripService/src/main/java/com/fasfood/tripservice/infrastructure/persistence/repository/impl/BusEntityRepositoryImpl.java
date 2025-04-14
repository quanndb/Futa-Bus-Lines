package com.fasfood.tripservice.infrastructure.persistence.repository.impl;

import com.fasfood.common.dto.response.StatisticResponse;
import com.fasfood.persistence.custom.AbstractPagingEntityRepository;
import com.fasfood.tripservice.domain.query.BusPagingQuery;
import com.fasfood.tripservice.infrastructure.persistence.entity.BusEntity;
import com.fasfood.util.QueryBuilder;

import java.util.List;

public class BusEntityRepositoryImpl extends AbstractPagingEntityRepository<BusEntity, BusPagingQuery, StatisticResponse> {
    protected BusEntityRepositoryImpl() {
        super(BusEntity.class);
    }

    @Override
    protected void createWhereClause(QueryBuilder queryBuilder, BusPagingQuery query) {
        queryBuilder.like(List.of("id", "licensePlate"), query.getKeyword())
                .whereIn("id", query.getIds())
                .whereIn("type", query.getTypes())
                .whereIn("licensePlate", query.getLicensePlates())
                .whereIn("status", query.getStatuses())
                .where("deleted", false);
    }
}
