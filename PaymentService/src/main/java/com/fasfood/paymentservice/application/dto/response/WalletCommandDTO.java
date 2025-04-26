package com.fasfood.paymentservice.application.dto.response;

import com.fasfood.common.dto.AuditableDTO;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletAction;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletCommandStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletCommandDTO extends AuditableDTO {
    private UUID id;
    private String code;
    private String bankCode;
    private String accountNumber;
    private String receiverName;
    private long amount;
    private WalletCommandStatus status;
    private WalletAction action;
    private UUID handlerId;
    private Instant handledAt;
    private Instant completedAt;
    private String paymentLink;
}
