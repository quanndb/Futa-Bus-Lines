package com.fasfood.paymentservice.domain.cmd;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WalletHistoryCreateCmd {
    private String bankCode;
    private String accountNumber;
    private String receiverName;
    private long amount;
    private String content;
}
