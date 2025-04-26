package com.fasfood.paymentservice.infrastructure.persistence.entity;

import com.fasfood.common.entity.CommonEntity;
import com.fasfood.common.validator.ValidateConstraint;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletAction;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletCommandStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "wallet_command")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletCommandEntity extends CommonEntity {
    @Column(name = "wallet_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID walletId;
    @Column(name = "code", length = ValidateConstraint.LENGTH.CODE_MAX_LENGTH, nullable = false)
    private String code;
    @Column(name = "bank_code", length = ValidateConstraint.LENGTH.CODE_MAX_LENGTH, nullable = false)
    private String bankCode;
    @Column(name = "account_number", length = ValidateConstraint.LENGTH.NAME_MAX_LENGTH, nullable = false)
    private String accountNumber;
    @Column(name = "receiver_name", length = ValidateConstraint.LENGTH.NAME_MAX_LENGTH, nullable = false)
    private String receiverName;
    @Column(name = "amount", nullable = false)
    private long amount;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    private WalletCommandStatus status;
    @Enumerated(EnumType.STRING)
    @Column(name = "action", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    private WalletAction action;
    @Column(name = "handler_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH)
    private UUID handlerId;
    @Column(name = "handled_at")
    private Instant handledAt;
    @Column(name = "completed_at")
    private Instant completedAt;
    @Column(name = "payment_link")
    private String paymentLink;
}
