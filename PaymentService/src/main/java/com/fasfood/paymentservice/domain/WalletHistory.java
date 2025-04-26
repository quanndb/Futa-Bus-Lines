package com.fasfood.paymentservice.domain;

import com.fasfood.common.domain.Domain;
import com.fasfood.paymentservice.domain.cmd.WalletHistoryCreateCmd;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletAction;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class WalletHistory extends Domain {
    private UUID walletId;
    private long currenBalance;
    private String bankCode;
    private String accountNumber;
    private String receiverName;
    private long amount;
    private String content;
    private WalletAction action;

    public WalletHistory(UUID walletId, long currenBalance, WalletAction action, WalletHistoryCreateCmd cmd) {
        super();
        this.walletId = walletId;
        this.currenBalance = currenBalance;
        this.bankCode = cmd.getBankCode();
        this.accountNumber = cmd.getAccountNumber();
        this.receiverName = cmd.getReceiverName();
        this.amount = cmd.getAmount();
        this.content = cmd.getContent();
        this.action = action;
    }
}
