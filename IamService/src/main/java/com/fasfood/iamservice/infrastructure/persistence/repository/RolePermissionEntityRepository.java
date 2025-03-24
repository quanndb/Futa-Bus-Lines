package com.fasfood.iamservice.infrastructure.persistence.repository;

import com.fasfood.iamservice.infrastructure.persistence.entity.RolePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RolePermissionEntityRepository extends JpaRepository<RolePermissionEntity, UUID> {
    @Override
    @Query("SELECT rp FROM RolePermissionEntity rp WHERE rp.id IN :ids AND rp.deleted = false")
    List<RolePermissionEntity> findAllById(@Param("ids") Iterable<UUID> ids);

    @Override
    @Query("SELECT rp FROM RolePermissionEntity rp WHERE rp.id = :id AND rp.deleted = false")
    Optional<RolePermissionEntity> findById(@Param("id") UUID id);

    @Query("SELECT rp FROM RolePermissionEntity rp WHERE rp.roleId IN :ids AND rp.deleted = false")
    List<RolePermissionEntity> findAllByRoleIds(@Param("ids") Iterable<UUID> ids);
}
