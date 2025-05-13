package com.fasfood.paymentservice.infrastructure.persistence.repository.impl;

import com.fasfood.common.dto.response.StatisticResponse;
import com.fasfood.paymentservice.domain.query.WalletCommandPagingQuery;
import com.fasfood.paymentservice.infrastructure.persistence.entity.WalletCommandEntity;
import com.fasfood.persistence.custom.AbstractPagingEntityRepository;
import com.fasfood.util.QueryBuilder;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WalletCommandEntityRepositoryImpl extends AbstractPagingEntityRepository<WalletCommandEntity, WalletCommandPagingQuery, StatisticResponse> {
    protected WalletCommandEntityRepositoryImpl() {
        super(WalletCommandEntity.class);
    }

    @Override
    public List<StatisticResponse> statistics(WalletCommandPagingQuery query) {
        QueryBuilder queryBuilder = QueryBuilder.select("SUM(a.amount)", this.entityClass.getSimpleName(), "a");
        this.createWhereClause(queryBuilder, query);
        Query entityManagerQuery = this.entityManager.createQuery(queryBuilder.build(), this.entityClass);
        queryBuilder.getParameters().forEach(entityManagerQuery::setParameter);
        List<StatisticResponse> responses = new ArrayList<>();
        var res = (Long) entityManagerQuery.getSingleResult();
        if (Objects.nonNull(res)) responses.add(new StatisticResponse("total", res));
        return responses;
    }

    @Override
    protected void createWhereClause(QueryBuilder queryBuilder, WalletCommandPagingQuery query) {
        queryBuilder.like(List.of("id", "code", "bankCode", "accountNumber", "receiverName", "amount"), query.getKeyword())
                .whereIn("id", query.getIds())
                .whereNotIn("id", query.getExcludedIds())
                .whereIn("action", query.getActions())
                .whereIn("status", query.getStatuses())
                .whereDate("createdAt", query.getTransactionDate())
                .whereTime("createdAt", query.getStartDate(), query.getEndDate())
                .where("walletId", query.getUserId())
                .where("deleted", false);
    }
}
