package com.fasfood.paymentservice.infrastructure.persistence.entity;

import com.fasfood.common.entity.CommonEntity;
import com.fasfood.common.validator.ValidateConstraint;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletAction;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "wallet_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletHistoryEntity extends CommonEntity {
    @Column(name = "wallet_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private UUID walletId;
    @Column(name = "current_balance", nullable = false)
    private long currenBalance;
    @Column(name = "bank_code", length = ValidateConstraint.LENGTH.CODE_MAX_LENGTH, nullable = false)
    private String bankCode;
    @Column(name = "account_number", length = ValidateConstraint.LENGTH.NAME_MAX_LENGTH, nullable = false)
    private String accountNumber;
    @Column(name = "receiver_name", length = ValidateConstraint.LENGTH.NAME_MAX_LENGTH, nullable = false)
    private String receiverName;
    @Column(name = "amount", nullable = false)
    private long amount;
    @Column(name = "content", length = ValidateConstraint.LENGTH.DESC_MAX_LENGTH, nullable = false)
    private String content;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    private WalletAction action;
}
