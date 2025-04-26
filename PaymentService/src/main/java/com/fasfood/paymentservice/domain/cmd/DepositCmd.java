package com.fasfood.paymentservice.domain.cmd;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositCmd {
    private long amount;
    private String paymentLink;
}
