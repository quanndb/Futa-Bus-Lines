package com.fasfood.tripservice.infrastructure.persistence.repository.impl;

import com.fasfood.common.dto.response.StatisticResponse;
import com.fasfood.persistence.custom.AbstractPagingEntityRepository;
import com.fasfood.tripservice.domain.query.RoutePagingQuery;
import com.fasfood.tripservice.infrastructure.persistence.entity.RouteEntity;
import com.fasfood.util.QueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RouteEntityRepositoryImpl extends AbstractPagingEntityRepository<RouteEntity, RoutePagingQuery, StatisticResponse> {
    protected RouteEntityRepositoryImpl() {
        super(RouteEntity.class);
    }

    @Override
    protected void createOrderByClause(QueryBuilder queryBuilder, RoutePagingQuery query) {
        List<String> sortBy = new ArrayList<>();
        sortBy.add("name.asc");
        if (Objects.nonNull(query.getSortBy())) {
            sortBy.add(query.getSortBy());
        }
        queryBuilder.orderBy(sortBy, "p1").limit(query.getPageIndex(), query.getPageSize());
    }

    @Override
    protected void createWhereClause(QueryBuilder queryBuilder, RoutePagingQuery query) {
        queryBuilder
                .leftJoin("TransitPointEntity", "p1", "departureId", "id")
                .leftJoin("TransitPointEntity", "p2", "destinationId", "id")
                .like(List.of("p1.name"), query.getDepartureKeyword())
                .like(List.of("p2.name"), query.getDestinationKeyword())
                .like(List.of("a.id", "a.departureId", "p1.name", "p1.address", "p2.name", "p2.address", "a.destinationId", "a.distance", "a.duration"), query.getKeyword())
                .whereIn("a.id", query.getIds())
                .where("a.deleted", false);
    }
}
