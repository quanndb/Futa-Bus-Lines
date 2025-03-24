package com.fasfood.iamservice.infrastructure.persistence.repository;

import com.fasfood.common.query.PagingQuery;
import com.fasfood.iamservice.infrastructure.persistence.entity.AccountEntity;
import com.fasfood.persistence.custom.CustomEntityRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountEntityRepository extends JpaRepository<AccountEntity, UUID>, CustomEntityRepository<AccountEntity, PagingQuery, Object> {

    @Override
    @Query("SELECT a FROM AccountEntity a WHERE a.id IN :ids AND a.deleted = false")
    List<AccountEntity> findAllById(@Param("ids") Iterable<UUID> ids);

    @Override
    @Query("SELECT a FROM AccountEntity a WHERE a.id = :id AND a.deleted = false")
    Optional<AccountEntity> findById(@Param("id") UUID id);

    @Query("SELECT a FROM AccountEntity a WHERE a.email = :email AND a.deleted = false")
    Optional<AccountEntity> findByEmail(@Param("email") String email);
}
