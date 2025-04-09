package com.fasfood.tripservice.infrastructure.persistence.repository.impl;

import com.fasfood.common.dto.response.StatisticResponse;
import com.fasfood.persistence.custom.AbstractPagingEntityRepository;
import com.fasfood.tripservice.domain.query.RoutePagingQuery;
import com.fasfood.tripservice.infrastructure.persistence.entity.RouteEntity;
import com.fasfood.util.QueryBuilder;

import java.util.List;

public class RouteEntityRepositoryImpl extends AbstractPagingEntityRepository<RouteEntity, RoutePagingQuery, StatisticResponse> {
    protected RouteEntityRepositoryImpl() {
        super(RouteEntity.class);
    }

    @Override
    protected void createWhereClause(QueryBuilder queryBuilder, RoutePagingQuery query) {
        queryBuilder
                .leftJoin("PlaceEntity", "p1", "departureCode", "code")
                .leftJoin("PlaceEntity", "p2", "destinationCode", "code")
                .like("p1.name", query.getDepartureKeyword())
                .like("p2.name", query.getDestinationKeyword())
                .like(List.of("a.id", "a.departureCode", "p1.name", "p2.name", "a.destinationCode", "a.distance", "a.duration"), query.getKeyword())
                .whereIn("a.id", query.getIds())
                .whereIn("a.departureCode", query.getDepartureCodes())
                .whereIn("a.destinationCode", query.getDestinationCodes())
                .where("a.deleted", false);
    }
}
