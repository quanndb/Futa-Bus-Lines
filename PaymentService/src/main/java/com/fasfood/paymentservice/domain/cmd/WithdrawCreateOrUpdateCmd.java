package com.fasfood.paymentservice.domain.cmd;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WithdrawCreateOrUpdateCmd {
    private String bankCode;
    private String accountNumber;
    private String receiverName;
    private long amount;
    private String paymentLink;
}
