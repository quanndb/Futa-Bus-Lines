package com.fasfood.bookingservice.infrastructure.persistence.repository.impl;

import com.fasfood.bookingservice.domain.query.BookingPagingQuery;
import com.fasfood.bookingservice.infrastructure.persistence.entity.BookingEntity;
import com.fasfood.bookingservice.infrastructure.persistence.repository.custom.CustomBookingEntityRepository;
import com.fasfood.common.dto.response.StatisticResponse;
import com.fasfood.persistence.custom.AbstractPagingEntityRepository;
import com.fasfood.util.QueryBuilder;
import jakarta.persistence.Query;

import java.util.List;

public class BookingEntityRepositoryImpl extends AbstractPagingEntityRepository<BookingEntity, BookingPagingQuery, StatisticResponse>
        implements CustomBookingEntityRepository {

    protected BookingEntityRepositoryImpl() {
        super(BookingEntity.class);
    }

    @Override
    protected void createWhereClause(QueryBuilder queryBuilder, BookingPagingQuery query) {
        queryBuilder.like(List.of("id", "code", "route", "phone", "email", "fullName"), query.getKeyword())
                .whereIn("id", query.getIds())
                .where("code", query.getCode())
                .where("status", query.getStatus())
                .where("departureDate", query.getDepartureDate())
                .where("deleted", false)
                .groupBy(List.of("code"));
    }

    @Override
    public List<String> searchCode(BookingPagingQuery query) {
        QueryBuilder queryBuilder = QueryBuilder.select("a.code", this.entityClass.getSimpleName(), "a");
        this.createWhereClause(queryBuilder, query);
        Query entityManagerQuery = this.entityManager.createQuery(queryBuilder.build(), this.entityClass);
        queryBuilder.getParameters().forEach(entityManagerQuery::setParameter);
        return (List<String>) entityManagerQuery.getResultList();
    }
}
