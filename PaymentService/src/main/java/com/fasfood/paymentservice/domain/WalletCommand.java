package com.fasfood.paymentservice.domain;

import com.fasfood.common.UserAuthentication;
import com.fasfood.common.domain.Domain;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.paymentservice.domain.cmd.DepositCmd;
import com.fasfood.paymentservice.domain.cmd.PayCmd;
import com.fasfood.paymentservice.domain.cmd.WithdrawCreateOrUpdateCmd;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletAction;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletCommandStatus;
import com.fasfood.paymentservice.infrastructure.support.exception.BadRequestError;
import com.fasfood.paymentservice.infrastructure.support.readmodel.TransactionReadModel;
import com.fasfood.util.RandomCodeUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class WalletCommand extends Domain {
    private UUID walletId;
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

    public WalletCommand(UUID walletId, WithdrawCreateOrUpdateCmd cmd) {
        super();
        this.walletId = walletId;
        this.code = RandomCodeUtil.generateOrderCode(TransactionReadModel.WITHDRAW_PREFIX);
        this.receiverName = cmd.getReceiverName();
        this.amount = cmd.getAmount();
        this.accountNumber = cmd.getAccountNumber();
        this.bankCode = cmd.getBankCode();
        this.status = WalletCommandStatus.WAIT_TO_RESOLVE;
        this.action = WalletAction.WITH_DRAW;
    }

    public WalletCommand(UUID walletId, DepositCmd cmd, UserAuthentication userAuthentication) {
        super();
        this.walletId = walletId;
        this.code = RandomCodeUtil.generateOrderCode(TransactionReadModel.DEPOSIT_PREFIX);
        this.receiverName = userAuthentication.getFullName();
        this.amount = cmd.getAmount();
        this.accountNumber = userAuthentication.getEmail();
        this.bankCode = TransactionReadModel.FUTA_WALLET;
        this.status = WalletCommandStatus.WAIT_TO_RESOLVE;
        this.action = WalletAction.DEPOSIT;
    }

    public WalletCommand(UUID walletId, PayCmd cmd) {
        super();
        this.walletId = walletId;
        this.code = cmd.getCode();
        this.receiverName = TransactionReadModel.FUTA;
        this.amount = cmd.getAmount();
        this.accountNumber = TransactionReadModel.FUTA;
        this.bankCode = TransactionReadModel.FUTA_WALLET;
        this.status = WalletCommandStatus.WAIT_TO_RESOLVE;
        this.action = WalletAction.USING;
    }

    public WalletCommand(UUID walletId, long amount) {
        super();
        this.walletId = walletId;
        this.code = RandomCodeUtil.generateOrderCode(TransactionReadModel.RETURN_PREFIX);
        this.receiverName = TransactionReadModel.FUTA;
        this.amount = amount;
        this.accountNumber = TransactionReadModel.FUTA;
        this.bankCode = TransactionReadModel.FUTA_WALLET;
        this.status = WalletCommandStatus.WAIT_TO_RESOLVE;
        this.action = WalletAction.RETURN;
    }

    public WalletCommand updateWithdrawCommand(WithdrawCreateOrUpdateCmd cmd) {
        if (!WalletCommandStatus.WAIT_TO_RESOLVE.equals(this.status)) {
            throw new ResponseException(BadRequestError.UNABLE_TO_RESOLVE);
        }
        this.receiverName = cmd.getReceiverName();
        this.amount = cmd.getAmount();
        this.accountNumber = cmd.getAccountNumber();
        this.bankCode = cmd.getBankCode();
        this.paymentLink = cmd.getPaymentLink();
        return this;
    }

    public WalletCommand resolve(UUID handlerId, WalletCommandStatus status) {
        if (!WalletCommandStatus.WAIT_TO_RESOLVE.equals(this.status) || WalletCommandStatus.SUCCESS.equals(status)) {
            throw new ResponseException(BadRequestError.UNABLE_TO_RESOLVE);
        }
        this.status = status;
        this.handlerId = handlerId;
        this.handledAt = Instant.now();
        return this;
    }

    public WalletCommand success() {
        if (!WalletCommandStatus.WAIT_TO_PAY.equals(this.status)) {
            throw new ResponseException(BadRequestError.UNABLE_TO_RESOLVE);
        }
        this.status = WalletCommandStatus.SUCCESS;
        this.completedAt = Instant.now();
        return this;
    }

    @Override
    public void delete() {
        if (!WalletCommandStatus.WAIT_TO_RESOLVE.equals(this.status)) {
            throw new ResponseException(BadRequestError.UNABLE_TO_RESOLVE);
        }
        super.delete();
    }

    public WalletCommand createPaymentLink(String paymentLink) {
        if (WalletAction.RETURN.equals(this.action)) {
            throw new ResponseException(BadRequestError.UNABLE_TO_RESOLVE);
        }
        this.paymentLink = paymentLink;
        return this;
    }

    public WalletCommand returnSuccessOrder(){
        if(!WalletAction.USING.equals(this.action)){
            throw new ResponseException(BadRequestError.UNABLE_TO_RESOLVE);
        }
        this.status = WalletCommandStatus.RETURNED;
        return this;
    }
}
