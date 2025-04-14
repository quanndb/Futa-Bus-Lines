package com.fasfood.persistence.custom;

import com.fasfood.common.dto.response.StatisticResponse;
import com.fasfood.common.entity.AuditableEntity;
import com.fasfood.common.query.PagingQuery;
import com.fasfood.util.QueryBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;

public abstract class AbstractPagingEntityRepository<E extends AuditableEntity, Q extends PagingQuery, S extends StatisticResponse>
        implements CustomEntityRepository<E, Q, S> {

    @PersistenceContext
    protected EntityManager entityManager;

    protected final Class<E> entityClass;

    protected AbstractPagingEntityRepository(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<E> search(Q query) {
        QueryBuilder queryBuilder = QueryBuilder.select("a", this.entityClass.getSimpleName(), "a");
        this.createWhereClause(queryBuilder, query);
        this.createOrderByClause(queryBuilder, query);
        Query entityManagerQuery = this.entityManager.createQuery(queryBuilder.build(), this.entityClass);
        queryBuilder.getParameters().forEach(entityManagerQuery::setParameter);
        return (List<E>) entityManagerQuery.getResultList();
    }

    @Override
    public Long count(Q query) {
        QueryBuilder queryBuilder = QueryBuilder.selectCountFrom(this.entityClass.getSimpleName(), "a");
        this.createWhereClause(queryBuilder, query);
        Query entityManagerQuery = this.entityManager.createQuery(queryBuilder.build(), this.entityClass);
        queryBuilder.getParameters().forEach(entityManagerQuery::setParameter);
        return (Long) entityManagerQuery.getSingleResult();
    }

    @Override
    public List<S> statistics(Q query) {
        return List.of();
    }

    @Override
    public List<E> autocomplete(Q query) {
        return List.of();
    }

    protected void createWhereClause(QueryBuilder queryBuilder, Q query) {
        queryBuilder.like(List.of("id"), query.getKeyword())
                .whereIn("id", query.getIds())
                .where("deleted", false);
    }

    protected void createOrderByClause(QueryBuilder queryBuilder, Q query) {
        queryBuilder.orderBy(query.getSortBy(), "a").limit(query.getPageIndex(), query.getPageSize());
    }
}
