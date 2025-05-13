package com.fasfood.paymentservice.domain.query;

import com.fasfood.common.query.PagingQuery;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletAction;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletCommandStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class WalletCommandPagingQuery extends PagingQuery {
    private UUID userId;
    private List<WalletAction> actions;
    private List<WalletCommandStatus> statuses;
    private LocalDate transactionDate;
    private LocalDate startDate;
    private LocalDate endDate;
}
