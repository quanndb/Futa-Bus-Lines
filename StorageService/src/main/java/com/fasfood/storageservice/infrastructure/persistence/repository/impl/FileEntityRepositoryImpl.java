package com.fasfood.storageservice.infrastructure.persistence.repository.impl;

import com.fasfood.persistence.custom.CustomEntityRepository;
import com.fasfood.storageservice.domain.query.FilePagingQuery;
import com.fasfood.storageservice.infrastructure.persistence.entity.FileEntity;
import com.fasfood.util.QueryBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;

public class FileEntityRepositoryImpl implements CustomEntityRepository<FileEntity, FilePagingQuery, Object> {

    private static final String TABLE_NAME = "FileEntity";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FileEntity> search(FilePagingQuery query) {
        QueryBuilder queryBuilder = QueryBuilder.select("a", TABLE_NAME, "a");
        this.createWhereClause(queryBuilder, query);
        queryBuilder.orderBy(query.getSortBy()).limit(query.getPageIndex(), query.getPageSize());
        Query entityManagerQuery = this.entityManager.createQuery(queryBuilder.build(), FileEntity.class);
        queryBuilder.getParameters().forEach(entityManagerQuery::setParameter);
        return entityManagerQuery.getResultList();
    }

    @Override
    public Long count(FilePagingQuery query) {
        QueryBuilder queryBuilder = QueryBuilder.selectCountFrom(TABLE_NAME, "a");
        this.createWhereClause(queryBuilder, query);
        Query entityManagerQuery = this.entityManager.createQuery(queryBuilder.build(), FileEntity.class);
        queryBuilder.getParameters().forEach(entityManagerQuery::setParameter);
        return (Long) entityManagerQuery.getSingleResult();
    }

    @Override
    public List<Object> statistics(FilePagingQuery query) {
        return List.of();
    }

    @Override
    public List<FileEntity> autocomplete(FilePagingQuery query) {
        return List.of();
    }

    private void createWhereClause(QueryBuilder queryBuilder, FilePagingQuery query) {
        queryBuilder.like(List.of("id", "ownerId", "name", "path", "type", "extension"), query.getKeyword())
                .whereIn("id", query.getIds())
                .whereTime("createdAt", query.getFromAt(), query.getToAt())
                .whereIn("sharing", query.getSharing())
                .whereIn("type", query.getType())
                .whereIn("extension", query.getExtension())
                .where("ownerId", query.getOwnerId())
                .where("deleted", false);
    }
}
