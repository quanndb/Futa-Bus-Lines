package com.fasfood.paymentservice.infrastructure.persistence.repository.impl;

import com.fasfood.common.dto.response.StatisticResponse;
import com.fasfood.paymentservice.domain.query.TransactionPagingQuery;
import com.fasfood.paymentservice.infrastructure.persistence.entity.TransactionEntity;
import com.fasfood.persistence.custom.AbstractPagingEntityRepository;
import com.fasfood.util.QueryBuilder;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransactionEntityRepositoryImpl extends AbstractPagingEntityRepository<TransactionEntity, TransactionPagingQuery, StatisticResponse> {
    protected TransactionEntityRepositoryImpl() {
        super(TransactionEntity.class);
    }

    @Override
    public List<StatisticResponse> statistics(TransactionPagingQuery query) {
        QueryBuilder queryBuilder = QueryBuilder.select("SUM(a.transferAmount)", this.entityClass.getSimpleName(), "a");
        this.createWhereClause(queryBuilder, query);
        Query entityManagerQuery = this.entityManager.createQuery(queryBuilder.build(), this.entityClass);
        queryBuilder.getParameters().forEach(entityManagerQuery::setParameter);
        List<StatisticResponse> responses = new ArrayList<>();
        var res = (Long) entityManagerQuery.getSingleResult();
        if (Objects.nonNull(res)) responses.add(new StatisticResponse("total", res));
        return responses;
    }

    @Override
    protected void createWhereClause(QueryBuilder queryBuilder, TransactionPagingQuery query) {
        queryBuilder.like(List.of("id", "gateway", "accountNumber", "code", "content", "transferAmount", "content", "referenceCode", "description"), query.getKeyword())
                .whereIn("id", query.getTransactionIds())
                .whereDate("createdAt", query.getTransactionDate())
                .whereTime("createdAt", query.getStartDate(), query.getEndDate())
                .whereIn("transferType", query.getTransferTypes());
    }
}
