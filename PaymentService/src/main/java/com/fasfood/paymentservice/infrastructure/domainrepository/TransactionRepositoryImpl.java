package com.fasfood.paymentservice.infrastructure.domainrepository;

import com.fasfood.common.exception.ResponseException;
import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.paymentservice.domain.Transaction;
import com.fasfood.paymentservice.domain.repository.TransactionRepository;
import com.fasfood.paymentservice.infrastructure.persistence.entity.TransactionEntity;
import com.fasfood.paymentservice.infrastructure.persistence.repository.TransactionEntityRepository;
import com.fasfood.paymentservice.infrastructure.support.exception.NotFoundError;
import com.fasfood.web.support.AbstractDomainRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepositoryImpl extends AbstractDomainRepository<Transaction, TransactionEntity, Long> implements TransactionRepository {

    private final TransactionEntityRepository transactionEntityRepository;

    protected TransactionRepositoryImpl(JpaRepository<TransactionEntity, Long> jpaRepository,
                                        EntityMapper<Transaction, TransactionEntity> mapper,
                                        TransactionEntityRepository transactionEntityRepository) {
        super(jpaRepository, mapper);
        this.transactionEntityRepository = transactionEntityRepository;
    }

    @Override
    public Transaction getById(Long id) {
        return this.mapper.toDomain(this.transactionEntityRepository
                .findById(id).orElseThrow(()->new ResponseException(NotFoundError.TRANSACTION_NOT_FOUND, id)));
    }
}
