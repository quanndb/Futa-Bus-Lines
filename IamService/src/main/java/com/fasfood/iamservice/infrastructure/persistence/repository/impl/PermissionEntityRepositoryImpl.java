package com.fasfood.iamservice.infrastructure.persistence.repository.impl;

import com.fasfood.iamservice.domain.query.PermissionPagingQuery;
import com.fasfood.iamservice.infrastructure.persistence.entity.PermissionEntity;
import com.fasfood.persistence.custom.CustomEntityRepository;
import com.fasfood.util.QueryBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;

public class PermissionEntityRepositoryImpl implements CustomEntityRepository<PermissionEntity, PermissionPagingQuery, Object> {

    private static final String TABLE_NAME = "PermissionEntity";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PermissionEntity> search(PermissionPagingQuery query) {
        QueryBuilder queryBuilder = QueryBuilder.select("p", TABLE_NAME, "p");
        this.createWhereClause(queryBuilder, query);
        queryBuilder.orderBy(query.getSortBy()).limit(query.getPageIndex(), query.getPageSize());
        Query entityManagerQuery = this.entityManager.createQuery(queryBuilder.build(), PermissionEntity.class);
        queryBuilder.getParameters().forEach(entityManagerQuery::setParameter);
        return entityManagerQuery.getResultList();
    }

    @Override
    public Long count(PermissionPagingQuery query) {
        QueryBuilder queryBuilder = QueryBuilder.selectCountFrom(TABLE_NAME, "p");
        this.createWhereClause(queryBuilder, query);
        Query entityManagerQuery = this.entityManager.createQuery(queryBuilder.build(), PermissionEntity.class);
        queryBuilder.getParameters().forEach(entityManagerQuery::setParameter);
        return (Long) entityManagerQuery.getSingleResult();
    }

    @Override
    public List<Object> statistics(PermissionPagingQuery query) {
        return List.of();
    }

    @Override
    public List<PermissionEntity> autocomplete(PermissionPagingQuery query) {
        return List.of();
    }

    private void createWhereClause(QueryBuilder queryBuilder, PermissionPagingQuery query) {
        queryBuilder.like(List.of("id", "name", "description", "code"), query.getKeyword())
                .whereIn("id", query.getIds())
                .where("deleted", false);
    }
}
