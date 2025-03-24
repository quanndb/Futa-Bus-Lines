package com.fasfood.storageservice.infrastructure.persistence.repository;

import com.fasfood.persistence.custom.CustomEntityRepository;
import com.fasfood.storageservice.domain.query.FileActionPagingQuery;
import com.fasfood.storageservice.infrastructure.persistence.entity.FileActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileActionEntityRepository extends JpaRepository<FileActionEntity, UUID>, CustomEntityRepository<FileActionEntity, FileActionPagingQuery, Object> {
    @Override
    @Query("SELECT a FROM FileActionEntity a WHERE a.id IN :ids AND a.deleted = false")
    List<FileActionEntity> findAllById(@Param("ids") Iterable<UUID> ids);

    @Override
    @Query("SELECT a FROM FileActionEntity a WHERE a.id = :id AND a.deleted = false")
    Optional<FileActionEntity> findById(@Param("id") UUID id);
}
