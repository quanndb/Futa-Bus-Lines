package com.fasfood.paymentservice.domain;

import com.fasfood.common.domain.AuditableDomain;
import com.fasfood.common.enums.TransferType;
import com.fasfood.paymentservice.domain.cmd.WebHookCmd;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class Transaction extends AuditableDomain {
    private long id;
    private String gateway;
    private LocalDateTime transactionDate;
    private String accountNumber;
    private String code;
    private String content;
    private TransferType transferType;
    private Long transferAmount;
    private Long accumulated;
    private String subAccount;
    private String referenceCode;
    private String description;

    public Transaction(WebHookCmd cmd){
        this.id = cmd.getId();
        this.gateway = cmd.getGateway();
        this.transactionDate = cmd.getTransactionDate();
        this.accountNumber = cmd.getAccountNumber();
        this.code = cmd.getCode();
        this.content = cmd.getContent();
        this.transferType = cmd.getTransferType();
        this.transferAmount = cmd.getTransferAmount();
        this.accumulated = cmd.getAccumulated();
        this.subAccount = cmd.getSubAccount();
        this.referenceCode = cmd.getReferenceCode();
        this.description = cmd.getDescription();
    }
}
