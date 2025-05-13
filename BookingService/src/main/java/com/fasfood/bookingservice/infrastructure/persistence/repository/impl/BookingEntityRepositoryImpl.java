package com.fasfood.bookingservice.infrastructure.persistence.repository.impl;

import com.fasfood.bookingservice.application.dto.response.BookedResponse;
import com.fasfood.bookingservice.domain.query.BookingPagingQuery;
import com.fasfood.bookingservice.infrastructure.persistence.entity.BookingEntity;
import com.fasfood.bookingservice.infrastructure.persistence.repository.custom.CustomBookingEntityRepository;
import com.fasfood.bookingservice.infrastructure.persistence.repository.projection.BookedProjection;
import com.fasfood.bookingservice.infrastructure.persistence.repository.projection.BookingProjection;
import com.fasfood.bookingservice.infrastructure.support.util.BookingTripCache;
import com.fasfood.common.dto.request.GetBookedRequest;
import com.fasfood.common.dto.response.StatisticResponse;
import com.fasfood.persistence.custom.AbstractPagingEntityRepository;
import com.fasfood.util.QueryBuilder;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class BookingEntityRepositoryImpl extends AbstractPagingEntityRepository<BookingEntity, BookingPagingQuery, StatisticResponse>
        implements CustomBookingEntityRepository {

    private final BookingTripCache bookingTripCache;

    protected BookingEntityRepositoryImpl(BookingTripCache bookingTripCache) {
        super(BookingEntity.class);
        this.bookingTripCache = bookingTripCache;
    }

    @Override
    protected void createWhereClause(QueryBuilder queryBuilder, BookingPagingQuery query) {
        queryBuilder.like(List.of("id", "code", "route", "phone", "email", "fullName"), query.getKeyword())
                .whereIn("id", query.getIds())
                .where("code", query.getCode())
                .where("status", query.getStatus())
                .where("departureDate", query.getDepartureDate())
                .where("deleted", false)
                .where("userId", query.getUserId());
    }

    @Override
    public List<String> searchCode(BookingPagingQuery query) {
        QueryBuilder sub = QueryBuilder.select("a.code, MAX(a.lastModifiedAt)", this.entityClass.getSimpleName(), "a");
        this.createWhereClause(sub, query);
        sub.groupBy(List.of("code"));
        List<String> sortBy = new ArrayList<>();
        sortBy.add("MAX(lastModifiedAt)");
        if (Objects.nonNull(query.getSortBy())) {
            sortBy.add(query.getSortBy());
        }
        sub.appendClause(" ORDER BY MAX(lastModifiedAt) DESC");
        sub.limit(query.getPageIndex(), query.getPageSize());
        Query entityManagerQuery = this.entityManager.createQuery(sub.build(), BookingProjection.class);
        sub.getParameters().forEach(entityManagerQuery::setParameter);
        List<BookingProjection> projections = (List<BookingProjection>) entityManagerQuery.getResultList();
        return projections.stream().map(BookingProjection::getCode).collect(Collectors.toList());
    }

    @Override
    public Long count(BookingPagingQuery query) {
        QueryBuilder sub = QueryBuilder.select("COUNT(DISTINCT a.code)", this.entityClass.getSimpleName(), "a");
        this.createWhereClause(sub, query);
        Query entityManagerQuery = this.entityManager.createQuery(sub.build(), this.entityClass);
        sub.getParameters().forEach(entityManagerQuery::setParameter);
        return (long) entityManagerQuery.getSingleResult();
    }

    @Override
    public List<BookedProjection> getBooked(GetBookedRequest request) {
        if (request.getDetailsIds().size() == 1 && this.bookingTripCache.hasKey(request)) {
            return this.bookingTripCache.get(request).getBookings();
        }
        String sql = "SELECT a.tripDetailsId AS tripDetailsId, t.seatNumber AS seatNumber " +
                "FROM BookingEntity a LEFT JOIN TicketEntity t ON t.bookingId = a.id " +
                "WHERE a.tripDetailsId IN :tripDetailsIds AND a.departureDate = :departureDate " +
                "AND a.deleted = false AND t.deleted = false AND a.status = 'PAYED'";
        Query entityManagerQuery = this.entityManager.createQuery(sql, BookedProjection.class);
        entityManagerQuery.setParameter("tripDetailsIds", request.getDetailsIds());
        entityManagerQuery.setParameter("departureDate", request.getStartDate());
        List<BookedProjection> booked = (List<BookedProjection>) entityManagerQuery.getResultList();
        if (request.getDetailsIds().size() == 1) {
            this.bookingTripCache.put(request, new BookedResponse(booked));
        }
        return booked;
    }
}
