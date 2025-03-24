package com.fasfood.storageservice.infrastructure.persistence.repository.impl;

import com.fasfood.persistence.custom.CustomEntityRepository;
import com.fasfood.storageservice.domain.query.FileActionPagingQuery;
import com.fasfood.storageservice.infrastructure.persistence.entity.FileActionEntity;
import com.fasfood.storageservice.infrastructure.persistence.entity.FileEntity;
import com.fasfood.util.QueryBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;

public class FileActionEntityRepositoryImpl implements CustomEntityRepository<FileEntity, FileActionPagingQuery, Object> {

    private static final String TABLE_NAME = "FileActionEntity";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FileEntity> search(FileActionPagingQuery query) {
        QueryBuilder queryBuilder = QueryBuilder.select("a", TABLE_NAME, "a");
        this.createWhereClause(queryBuilder, query);
        queryBuilder.orderBy(query.getSortBy()).limit(query.getPageIndex(), query.getPageSize());
        Query entityManagerQuery = this.entityManager.createQuery(queryBuilder.build(), FileActionEntity.class);
        queryBuilder.getParameters().forEach(entityManagerQuery::setParameter);
        return entityManagerQuery.getResultList();
    }

    @Override
    public Long count(FileActionPagingQuery query) {
        QueryBuilder queryBuilder = QueryBuilder.selectCountFrom(TABLE_NAME, "a");
        this.createWhereClause(queryBuilder, query);
        Query entityManagerQuery = this.entityManager.createQuery(queryBuilder.build(), FileActionEntity.class);
        queryBuilder.getParameters().forEach(entityManagerQuery::setParameter);
        return (Long) entityManagerQuery.getSingleResult();
    }

    @Override
    public List<Object> statistics(FileActionPagingQuery query) {
        return List.of();
    }

    @Override
    public List<FileEntity> autocomplete(FileActionPagingQuery query) {
        return List.of();
    }

    private void createWhereClause(QueryBuilder queryBuilder, FileActionPagingQuery query) {
        queryBuilder.like(List.of("id", "fileId", "createdAt", "senderId", "action"), query.getKeyword())
                .whereIn("id", query.getIds())
                .whereTime("createdAt", query.getFromAt(), query.getToAt())
                .whereIn("action", query.getActions())
                .whereIn("fileId", query.getFileId())
                .whereIn("senderId", query.getSenderId())
                .where("deleted", false);
    }
}
