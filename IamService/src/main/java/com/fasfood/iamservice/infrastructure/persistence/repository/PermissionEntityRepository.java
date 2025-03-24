package com.fasfood.iamservice.infrastructure.persistence.repository;

import com.fasfood.common.query.PagingQuery;
import com.fasfood.iamservice.infrastructure.persistence.entity.PermissionEntity;
import com.fasfood.persistence.custom.CustomEntityRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionEntityRepository extends JpaRepository<PermissionEntity, UUID>, CustomEntityRepository<PermissionEntity, PagingQuery, Object> {

    @Override
    @Query("SELECT p FROM PermissionEntity p WHERE p.id IN :ids AND p.deleted = false")
    List<PermissionEntity> findAllById(@Param("ids") Iterable<UUID> ids);

    @Override
    @Query("SELECT p FROM PermissionEntity p WHERE p.id = :id AND p.deleted = false")
    Optional<PermissionEntity> findById(@Param("id") UUID id);

    @Query("SELECT p FROM PermissionEntity p WHERE p.id IN :ids AND p.deleted = false")
    List<PermissionEntity> findExistedEntitiesByIds(Iterable<UUID> ids);

    @Query("SELECT p FROM PermissionEntity p WHERE p.code IN :codes AND p.deleted = false")
    List<PermissionEntity> findExistedEntitiesByCodes(Iterable<String> codes);
}
