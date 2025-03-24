package com.fasfood.iamservice.infrastructure.persistence.repository;

import com.fasfood.common.query.PagingQuery;
import com.fasfood.iamservice.infrastructure.persistence.entity.RoleEntity;
import com.fasfood.persistence.custom.CustomEntityRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleEntityRepository extends JpaRepository<RoleEntity, UUID>, CustomEntityRepository<RoleEntity, PagingQuery, Object> {
    @Override
    @Query("SELECT r FROM RoleEntity r WHERE r.id IN :ids AND r.deleted = false")
    List<RoleEntity> findAllById(@Param("ids") Iterable<UUID> ids);

    @Override
    @Query("SELECT r FROM RoleEntity r WHERE r.id = :id AND r.deleted = false")
    Optional<RoleEntity> findById(@Param("id") UUID id);

    @Query("SELECT r FROM RoleEntity r WHERE r.name IN :names AND r.deleted = false")
    List<RoleEntity> findAllExistedByNames(@Param("names") Iterable<String> names);
}
