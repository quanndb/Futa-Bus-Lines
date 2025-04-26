package com.fasfood.paymentservice.domain.cmd;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayCmd {
    private Boolean isUseWallet;
    private String code;
    private long amount;
    private String paymentLink;
}
