package com.fasfood.paymentservice.infrastructure.persistence.repository.impl;

import com.fasfood.common.dto.response.StatisticResponse;
import com.fasfood.paymentservice.domain.query.WalletHistoryPagingQuery;
import com.fasfood.paymentservice.infrastructure.persistence.entity.WalletHistoryEntity;
import com.fasfood.persistence.custom.AbstractPagingEntityRepository;
import com.fasfood.util.QueryBuilder;

import java.util.List;

public class WalletHistoryEntityRepositoryImpl extends AbstractPagingEntityRepository<WalletHistoryEntity, WalletHistoryPagingQuery, StatisticResponse> {
    protected WalletHistoryEntityRepositoryImpl() {
        super(WalletHistoryEntity.class);
    }

    @Override
    protected void createWhereClause(QueryBuilder queryBuilder, WalletHistoryPagingQuery query) {
        queryBuilder.like(List.of("id", "currenBalance", "bankCode", "accountNumber", "receiverName", "amount", "content"), query.getKeyword())
                .whereIn("id", query.getIds())
                .whereNotIn("id", query.getExcludedIds())
                .where("action", query.getActions())
                .whereDate("createdAt", query.getTransactionDate())
                .where("walletId", query.getUserId())
                .where("deleted", false);
    }
}
