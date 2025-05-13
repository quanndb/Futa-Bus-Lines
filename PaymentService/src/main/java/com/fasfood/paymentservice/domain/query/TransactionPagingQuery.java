package com.fasfood.paymentservice.domain.query;

import com.fasfood.common.enums.TransferType;
import com.fasfood.common.query.PagingQuery;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TransactionPagingQuery extends PagingQuery {
    private LocalDate transactionDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Long> transactionIds;
    private List<TransferType> transferTypes;
}
