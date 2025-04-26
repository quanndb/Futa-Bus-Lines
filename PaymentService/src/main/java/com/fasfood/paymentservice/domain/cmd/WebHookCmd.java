package com.fasfood.paymentservice.domain.cmd;

import com.fasfood.common.enums.TransferType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class WebHookCmd {
    private long id;
    private String gateway;
    private String accountNumber;
    private LocalDateTime transactionDate;
    private String code;
    private String content;
    private TransferType transferType;
    private Long transferAmount;
    private Long accumulated;
    private String subAccount;
    private String referenceCode;
    private String description;
}
