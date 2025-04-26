package com.fasfood.paymentservice.application.dto.request;

import com.fasfood.common.dto.request.PagingRequest;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletAction;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class WalletHistoryPagingRequest extends PagingRequest {
    private List<WalletAction> actions;
    private LocalDate transactionDate;
}
