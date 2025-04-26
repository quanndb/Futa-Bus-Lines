package com.fasfood.common.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TransactionDTO {
    private String id;
    private LocalDateTime transactionDate;
    private String accountNumber;
    private String subAccount;
    private String amountIn;
    private String amountOut;
    private String accumulated;
    private String code;
    private String transactionContent;
    private String referenceNumber;
    private String bankBrandName;
    private String bankAccountId;
}
