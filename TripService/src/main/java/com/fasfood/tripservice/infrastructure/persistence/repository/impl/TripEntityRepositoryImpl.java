package com.fasfood.tripservice.infrastructure.persistence.repository.impl;

import com.fasfood.common.dto.response.StatisticResponse;
import com.fasfood.common.enums.BusTypeEnum;
import com.fasfood.persistence.custom.AbstractPagingEntityRepository;
import com.fasfood.tripservice.application.dto.request.FormTimeToTimeRequest;
import com.fasfood.tripservice.application.dto.request.TripFilterRequest;
import com.fasfood.tripservice.domain.query.TripPagingQuery;
import com.fasfood.tripservice.infrastructure.persistence.entity.TripEntity;
import com.fasfood.tripservice.infrastructure.persistence.repository.custom.CustomTripEntityRepository;
import com.fasfood.tripservice.infrastructure.support.enums.TripOrderBy;
import com.fasfood.util.QueryBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class TripEntityRepositoryImpl extends AbstractPagingEntityRepository<TripEntity, TripPagingQuery, StatisticResponse>
        implements CustomTripEntityRepository {

    @PersistenceContext
    protected EntityManager entityManager;

    protected TripEntityRepositoryImpl() {
        super(TripEntity.class);
    }

    @Override
    protected void createWhereClause(QueryBuilder queryBuilder, TripPagingQuery query) {
        queryBuilder.like(List.of("id", "code"), query.getKeyword())
                .whereIn("code", query.getCodes())
                .where("deleted", false);
    }

    @Override
    public List<UUID> findTrips(TripFilterRequest filter) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("SELECT td.id FROM TripDetailsEntity td");
        this.createWhereClause(sql, params, filter);
        this.createBusTypeClause(sql, params, filter.getBusType());
        this.createDepartureTimeClause(sql, params, filter.getDepartureTime());
        this.createOrderByClause(sql, filter.getOrderBy());
        Query entityManagerQuery = this.entityManager.createQuery(sql.toString(), TripEntity.class);
        params.forEach(entityManagerQuery::setParameter);
        return (List<UUID>) entityManagerQuery.getResultList();
    }

    private void createWhereClause(StringBuilder sql, Map<String, Object> params, TripFilterRequest filter) {
        params.put("departureId", filter.getDepartureId());
        params.put("destinationId", filter.getDestinationId());
        params.put("departureDate", filter.getDepartureDate());
        params.put("now", new Timestamp(System.currentTimeMillis()));
        sql.append(" left join TripEntity t on td.tripId = t.id ");
        sql.append(" left join TripTransitEntity ttDep on ttDep.tripId = t.id");
        sql.append(" left join TripTransitEntity ttDes on ttDes.tripId = t.id");
        sql.append(" left join BusTypeEntity bt on td.type = bt.type ");
        sql.append(" WHERE t.deleted = false AND td.deleted = false AND td.status = 'ACTIVE' AND ttDep.deleted = false AND ttDes.deleted = false AND bt.deleted = false ");
        sql.append(" AND ttDep.transitPointId = :departureId ");
        sql.append(" AND ttDes.transitPointId = :destinationId ");
        sql.append(" AND :departureDate BETWEEN DATE(td.fromDate) AND DATE(td.toDate) ");
        sql.append(" AND ttDep.transitOrder < ttDes.transitOrder");
        sql.append(" AND (CAST(:departureDate || ' ' || ttDep.arrivalTime AS timestamp)) > :now");
        if (!CollectionUtils.isEmpty(filter.getDetailsIds())) {
            params.put("detailsIds", filter.getDetailsIds());
            sql.append(" AND td.id IN :detailsIds ");
        }
    }

    private void createDepartureTimeClause(StringBuilder sql, Map<String, Object> params, FormTimeToTimeRequest request) {
        if (Objects.isNull(request) || Objects.isNull(request.getFromTime()) || Objects.isNull(request.getToTime())) {
            return;
        }
        sql.append(" AND ttDep.arrivalTime between :fromTime AND :toTime ");
        params.put("fromTime", request.getFromTime());
        params.put("toTime", request.getToTime());
    }

    private void createBusTypeClause(StringBuilder sql, Map<String, Object> params, List<BusTypeEnum> types) {
        if (CollectionUtils.isEmpty(types)) {
            return;
        }
        sql.append(" AND bt.type IN (:busTypes) ");
        params.put("busTypes", types);
    }

    private void createOrderByClause(StringBuilder sql, List<TripOrderBy> orderBys) {
        if (CollectionUtils.isEmpty(orderBys)) {
            return;
        }
        sql.append(" ORDER BY ");
        List<String> orders = new ArrayList<>();
        if (orderBys.contains(TripOrderBy.PRICE)) {
            orders.add(" td.price ASC ");
        }
        if (orderBys.contains(TripOrderBy.DEPARTURE_TIME)) {
            orders.add(" ttDep.arrivalTime ASC ");
        }
        sql.append(String.join(", ", orders));
    }
}
