package com.fasfood.paymentservice.infrastructure.persistence.repository;

import com.fasfood.paymentservice.infrastructure.persistence.entity.TransactionEntity;
import com.fasfood.persistence.custom.EntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransactionEntityRepository extends EntityRepository<TransactionEntity, Long> {
    @Override
    @Query("SELECT a FROM TransactionEntity a WHERE a.id IN :ids")
    List<TransactionEntity> findAllById(@Param("ids") Iterable<Long> ids);

    @Override
    @Query("SELECT a FROM TransactionEntity a WHERE a.id = :id")
    Optional<TransactionEntity> findById(@Param("id") Long id);
}
