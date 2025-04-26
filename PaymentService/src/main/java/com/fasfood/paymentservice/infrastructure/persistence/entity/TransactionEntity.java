package com.fasfood.paymentservice.infrastructure.persistence.entity;

import com.fasfood.common.entity.AuditableEntity;
import com.fasfood.common.enums.TransferType;
import com.fasfood.common.validator.ValidateConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private Long id;
    @Column(name = "gateway", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private String gateway;
    @Column(name = "transaction_date",nullable = false)
    private LocalDateTime transactionDate;
    @Column(name = "account_number", length = ValidateConstraint.LENGTH.NAME_MAX_LENGTH, nullable = false)
    private String accountNumber;
    @Column(name = "code", length = ValidateConstraint.LENGTH.CODE_MAX_LENGTH)
    private String code;
    @Column(name = "content", length = ValidateConstraint.LENGTH.DESC_MAX_LENGTH, nullable = false)
    private String content;
    @Enumerated(EnumType.STRING)
    @Column(name = "transfer_type", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    private TransferType transferType;
    @Column(name = "transfer_amount", nullable = false)
    private Long transferAmount;
    @Column(name = "accumulated")
    private Long accumulated;
    @Column(name = "sub_account", length = ValidateConstraint.LENGTH.NAME_MAX_LENGTH)
    private String subAccount;
    @Column(name = "reference_code", length = ValidateConstraint.LENGTH.CODE_MAX_LENGTH, nullable = false)
    private String referenceCode;
    @Column(name = "description", length = ValidateConstraint.LENGTH.DESC_MAX_LENGTH)
    private String description;
}
