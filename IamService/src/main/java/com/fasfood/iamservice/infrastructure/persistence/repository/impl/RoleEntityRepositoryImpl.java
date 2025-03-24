package com.fasfood.iamservice.infrastructure.persistence.repository.impl;

import com.fasfood.iamservice.domain.query.RolePagingQuery;
import com.fasfood.iamservice.infrastructure.persistence.entity.RoleEntity;
import com.fasfood.persistence.custom.CustomEntityRepository;
import com.fasfood.util.QueryBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;

public class RoleEntityRepositoryImpl implements CustomEntityRepository<RoleEntity, RolePagingQuery, Object> {

    private static final String TABLE_NAME = "RoleEntity";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<RoleEntity> search(RolePagingQuery query) {
        QueryBuilder queryBuilder = QueryBuilder.select("r", TABLE_NAME, "r");
        this.createWhereClause(queryBuilder, query);
        queryBuilder.orderBy(query.getSortBy()).limit(query.getPageIndex(), query.getPageSize());
        Query entityManagerQuery = this.entityManager.createQuery(queryBuilder.build(), RoleEntity.class);
        queryBuilder.getParameters().forEach(entityManagerQuery::setParameter);
        return entityManagerQuery.getResultList();
    }

    @Override
    public Long count(RolePagingQuery query) {
        QueryBuilder queryBuilder = QueryBuilder.selectCountFrom(TABLE_NAME, "r");
        this.createWhereClause(queryBuilder, query);
        Query entityManagerQuery = this.entityManager.createQuery(queryBuilder.build(), RoleEntity.class);
        queryBuilder.getParameters().forEach(entityManagerQuery::setParameter);
        return (Long) entityManagerQuery.getSingleResult();
    }

    @Override
    public List<Object> statistics(RolePagingQuery query) {
        return List.of();
    }

    @Override
    public List<RoleEntity> autocomplete(RolePagingQuery query) {
        return List.of();
    }

    private void createWhereClause(QueryBuilder queryBuilder, RolePagingQuery query) {
        queryBuilder.like(List.of("id", "name", "description"), query.getKeyword())
                .whereIn("id", query.getIds())
                .whereIn("isRoot", query.getIsRoots())
                .where("deleted", false);
    }
}
