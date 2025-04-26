package com.fasfood.paymentservice.application.dto.response;

import com.fasfood.common.dto.AuditableDTO;
import com.fasfood.common.enums.TransferType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionDTO extends AuditableDTO {
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
}
