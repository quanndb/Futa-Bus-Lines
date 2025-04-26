package com.fasfood.paymentservice.infrastructure.persistence.repository;

import com.fasfood.paymentservice.infrastructure.persistence.entity.WalletCommandEntity;
import com.fasfood.persistence.custom.EntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletCommandEntityRepository extends EntityRepository<WalletCommandEntity, UUID> {
    @Override
    @Query("SELECT a FROM WalletCommandEntity a WHERE a.id IN :ids AND a.deleted = false")
    List<WalletCommandEntity> findAllById(@Param("ids") Iterable<UUID> ids);

    @Override
    @Query("SELECT a FROM WalletCommandEntity a WHERE a.id = :id AND a.deleted = false")
    Optional<WalletCommandEntity> findById(@Param("id") UUID id);

    @Query("SELECT a FROM WalletCommandEntity a WHERE a.code = :code AND a.deleted = false ORDER BY a.createdAt DESC LIMIT 1")
    Optional<WalletCommandEntity> findByCode(@Param("code") String code);
}
