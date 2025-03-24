package com.fasfood.iamservice.infrastructure.persistence.repository.impl;

import com.fasfood.iamservice.domain.query.AccountPagingQuery;
import com.fasfood.iamservice.infrastructure.persistence.entity.AccountEntity;
import com.fasfood.persistence.custom.CustomEntityRepository;
import com.fasfood.util.QueryBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;

public class AccountEntityRepositoryImpl implements CustomEntityRepository<AccountEntity, AccountPagingQuery, Object> {

    private static final String TABLE_NAME = "AccountEntity";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AccountEntity> search(AccountPagingQuery query) {
        QueryBuilder queryBuilder = QueryBuilder.select("a", TABLE_NAME, "a");
        this.createWhereClause(queryBuilder, query);
        queryBuilder.orderBy(query.getSortBy()).limit(query.getPageIndex(), query.getPageSize());
        Query entityManagerQuery = this.entityManager.createQuery(queryBuilder.build(), AccountEntity.class);
        queryBuilder.getParameters().forEach(entityManagerQuery::setParameter);
        return entityManagerQuery.getResultList();
    }

    @Override
    public Long count(AccountPagingQuery query) {
        QueryBuilder queryBuilder = QueryBuilder.selectCountFrom(TABLE_NAME, "a");
        this.createWhereClause(queryBuilder, query);
        Query entityManagerQuery = this.entityManager.createQuery(queryBuilder.build(), AccountEntity.class);
        queryBuilder.getParameters().forEach(entityManagerQuery::setParameter);
        return (Long) entityManagerQuery.getSingleResult();
    }

    @Override
    public List<Object> statistics(AccountPagingQuery query) {
        return List.of();
    }

    @Override
    public List<AccountEntity> autocomplete(AccountPagingQuery query) {
        return List.of();
    }

    private void createWhereClause(QueryBuilder queryBuilder, AccountPagingQuery query) {
        queryBuilder.like(List.of("id", "email", "fullName", "phoneNumber"), query.getKeyword())
                .whereIn("id", query.getIds())
                .whereIn("status", query.getStatus())
                .whereIn("gender", query.getGender())
                .where("deleted", false);
    }
}
