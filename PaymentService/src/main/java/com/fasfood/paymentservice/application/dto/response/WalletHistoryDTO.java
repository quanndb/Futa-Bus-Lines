package com.fasfood.paymentservice.application.dto.response;

import com.fasfood.common.dto.AuditableDTO;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletAction;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class WalletHistoryDTO extends AuditableDTO {
    private UUID id;
    private UUID walletId;
    private long currenBalance;
    private String bankCode;
    private String accountNumber;
    private String receiverName;
    private long amount;
    private String content;
    private WalletAction action;
}
