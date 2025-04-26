package com.fasfood.paymentservice.infrastructure.persistence.repository.impl;

import com.fasfood.common.dto.response.StatisticResponse;
import com.fasfood.paymentservice.domain.query.WalletCommandPagingQuery;
import com.fasfood.paymentservice.infrastructure.persistence.entity.WalletCommandEntity;
import com.fasfood.persistence.custom.AbstractPagingEntityRepository;
import com.fasfood.util.QueryBuilder;

import java.util.List;

public class WalletCommandEntityRepositoryImpl extends AbstractPagingEntityRepository<WalletCommandEntity, WalletCommandPagingQuery, StatisticResponse> {
    protected WalletCommandEntityRepositoryImpl() {
        super(WalletCommandEntity.class);
    }

    @Override
    protected void createWhereClause(QueryBuilder queryBuilder, WalletCommandPagingQuery query) {
        queryBuilder.like(List.of("id", "code", "bankCode", "accountNumber", "receiverName", "amount", "content"), query.getKeyword())
                .whereIn("id", query.getIds())
                .whereNotIn("id", query.getExcludedIds())
                .where("action", query.getActions())
                .whereIn("status", query.getStatuses())
                .whereDate("createdAt", query.getTransactionDate())
                .where("walletId", query.getUserId())
                .where("deleted", false);
    }
}
