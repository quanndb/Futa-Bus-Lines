package com.fasfood.paymentservice.infrastructure.persistence.repository;

import com.fasfood.paymentservice.infrastructure.persistence.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletEntityRepository extends JpaRepository<WalletEntity, UUID> {
    @Override
    @Query("SELECT a FROM WalletEntity a WHERE a.id IN :ids AND a.deleted = false")
    List<WalletEntity> findAllById(@Param("ids") Iterable<UUID> ids);

    @Override
    @Query("SELECT a FROM WalletEntity a WHERE a.id = :id AND a.deleted = false")
    Optional<WalletEntity> findById(@Param("id") UUID id);

    @Query("SELECT a FROM WalletEntity a WHERE a.userId = :userId AND a.deleted = false")
    Optional<WalletEntity> findByUserId(UUID userId);
}
