package com.fasfood.paymentservice.domain.query;

import com.fasfood.common.query.PagingQuery;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletAction;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class WalletHistoryPagingQuery extends PagingQuery {
    private List<WalletAction> actions;
    private LocalDate transactionDate;
    private UUID userId;
}
