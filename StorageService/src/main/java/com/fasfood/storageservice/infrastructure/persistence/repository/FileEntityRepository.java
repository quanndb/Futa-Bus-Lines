package com.fasfood.storageservice.infrastructure.persistence.repository;

import com.fasfood.common.query.PagingQuery;
import com.fasfood.persistence.custom.CustomEntityRepository;
import com.fasfood.storageservice.infrastructure.persistence.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileEntityRepository extends JpaRepository<FileEntity, UUID>, CustomEntityRepository<FileEntity, PagingQuery, Object> {
    @Override
    @Query("SELECT a FROM FileEntity a WHERE a.id IN :ids AND a.deleted = false")
    List<FileEntity> findAllById(@Param("ids") Iterable<UUID> ids);

    @Override
    @Query("SELECT a FROM FileEntity a WHERE a.id = :id AND a.deleted = false")
    Optional<FileEntity> findById(@Param("id") UUID id);
}
