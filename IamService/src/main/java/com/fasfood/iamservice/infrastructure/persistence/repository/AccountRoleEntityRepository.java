package com.fasfood.iamservice.infrastructure.persistence.repository;

import com.fasfood.iamservice.infrastructure.persistence.entity.AccountRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRoleEntityRepository extends JpaRepository<AccountRoleEntity, UUID> {
    @Override
    @Query("SELECT ar FROM AccountRoleEntity ar WHERE ar.id IN :ids AND ar.deleted = false")
    List<AccountRoleEntity> findAllById(@Param("ids") Iterable<UUID> ids);

    @Override
    @Query("SELECT ar FROM AccountRoleEntity ar WHERE ar.id = :id AND ar.deleted = false")
    Optional<AccountRoleEntity> findById(@Param("id") UUID id);

    @Query("SELECT ar FROM AccountRoleEntity ar WHERE ar.accountId IN :ids AND ar.deleted = false")
    List<AccountRoleEntity> findAllByAccountId(@Param("ids") Iterable<UUID> ids);
}
