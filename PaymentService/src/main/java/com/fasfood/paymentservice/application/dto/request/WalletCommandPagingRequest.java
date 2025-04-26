package com.fasfood.paymentservice.application.dto.request;

import com.fasfood.common.dto.request.PagingRequest;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletAction;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletCommandStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class WalletCommandPagingRequest extends PagingRequest {
    private List<WalletAction> actions;
    private List<WalletCommandStatus> statuses;
    private LocalDate transactionDate;
}
