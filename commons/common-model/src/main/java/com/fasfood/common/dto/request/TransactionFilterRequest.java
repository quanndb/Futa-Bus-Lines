package com.fasfood.common.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionFilterRequest extends Request{
    private String accountNumber;
    private LocalDateTime transactionDateMin;
    private LocalDateTime transactionDateMax;
    private Long sinceId;
    private Long limit;
    private String referenceNumber;
    private Long amountIn;
    private Long amountOut;
}
