package com.fasfood.paymentservice.infrastructure.persistence.repository.impl;

import com.fasfood.common.dto.response.StatisticResponse;
import com.fasfood.paymentservice.domain.query.TransactionPagingQuery;
import com.fasfood.paymentservice.infrastructure.persistence.entity.TransactionEntity;
import com.fasfood.persistence.custom.AbstractPagingEntityRepository;
import com.fasfood.util.QueryBuilder;

import java.util.List;

public class TransactionEntityRepositoryImpl extends AbstractPagingEntityRepository<TransactionEntity, TransactionPagingQuery, StatisticResponse> {
    protected TransactionEntityRepositoryImpl() {
        super(TransactionEntity.class);
    }

    @Override
    protected void createWhereClause(QueryBuilder queryBuilder, TransactionPagingQuery query) {
        queryBuilder.like(List.of("id", "gateway", "accountNumber", "code", "content", "amount", "content", "referenceCode", "description"), query.getKeyword())
                .whereIn("id", query.getTransactionIds())
                .whereDate("createdAt", query.getTransactionDate())
                .whereIn("transferType", query.getTransferTypes());
    }
}
