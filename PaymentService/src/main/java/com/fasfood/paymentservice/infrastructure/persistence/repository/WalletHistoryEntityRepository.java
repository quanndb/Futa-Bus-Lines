package com.fasfood.paymentservice.infrastructure.persistence.repository;

import com.fasfood.paymentservice.infrastructure.persistence.entity.WalletHistoryEntity;
import com.fasfood.persistence.custom.EntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletHistoryEntityRepository extends EntityRepository<WalletHistoryEntity, UUID> {
    @Override
    @Query("SELECT a FROM WalletHistoryEntity a WHERE a.id IN :ids AND a.deleted = false")
    List<WalletHistoryEntity> findAllById(@Param("ids") Iterable<UUID> ids);

    @Override
    @Query("SELECT a FROM WalletHistoryEntity a WHERE a.id = :id AND a.deleted = false")
    Optional<WalletHistoryEntity> findById(@Param("id") UUID id);
}
