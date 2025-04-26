package com.fasfood.paymentservice.domain;

import com.fasfood.common.UserAuthentication;
import com.fasfood.common.constant.BookingReadModel;
import com.fasfood.common.domain.Domain;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.paymentservice.domain.cmd.DepositCmd;
import com.fasfood.paymentservice.domain.cmd.PayCmd;
import com.fasfood.paymentservice.domain.cmd.WalletHistoryCreateCmd;
import com.fasfood.paymentservice.domain.cmd.WithdrawCreateOrUpdateCmd;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletAction;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletCommandStatus;
import com.fasfood.paymentservice.infrastructure.support.exception.BadRequestError;
import com.fasfood.paymentservice.infrastructure.support.exception.NotFoundError;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class Wallet extends Domain {
    private UUID userId;
    private long currentBalance;
    private List<WalletHistory> histories;
    private List<WalletCommand> commands;

    public Wallet(UUID userId) {
        super();
        this.id = userId;
        this.userId = userId;
        this.currentBalance = 0L;
    }

    // command
    public WalletCommand createWithdrawCommand(UserAuthentication authentication, WithdrawCreateOrUpdateCmd cmd) {
        this.checkWalletOwner(authentication.getUserId());
        if (cmd.getAmount() > this.currentBalance) {
            throw new ResponseException(BadRequestError.INSUFFICIENT_BALANCE);
        }
        WalletCommand command = new WalletCommand(this.id, cmd);
        this.commands.add(command);
        return command;
    }

    public WalletCommand createDepositCommand(UserAuthentication authentication, DepositCmd cmd, Function<WalletCommand, String> createPaymentLinkFn) {
        this.checkWalletOwner(authentication.getUserId());
        WalletCommand command = new WalletCommand(this.id, cmd, authentication);
        command.resolve(authentication.getUserId(), WalletCommandStatus.WAIT_TO_PAY);
        command.createPaymentLink(createPaymentLinkFn.apply(command));
        this.commands.add(command);
        return command;
    }

    public WalletCommand createPayCommand(PayCmd cmd, Function<WalletCommand, String> createPaymentLinkFn) {
        WalletCommand command = new WalletCommand(this.id, cmd);
        command.resolve(userId, WalletCommandStatus.WAIT_TO_PAY);
        if (cmd.getIsUseWallet()) {
            if (this.currentBalance >= cmd.getAmount()) {
                this.minusMoney(this.from(command));
                command.success();
            } else {
                this.minusMoney(this.from(command, this.currentBalance));
                long haveToPay = cmd.getAmount() - this.currentBalance;
                if (haveToPay < 10000) throw new ResponseException(BadRequestError.PAYMENT_LINK_AT_LEAST);
                command.createPaymentLink(createPaymentLinkFn.apply(WalletCommand.builder()
                        .code(command.getCode())
                        .amount(haveToPay)
                        .action(WalletAction.USING)
                        .build()));
            }
            this.commands.add(command);
            return command;
        }
        command.createPaymentLink(createPaymentLinkFn.apply(command));
        this.commands.add(command);
        return command;
    }

    public WalletCommand createReturnCommand(UUID userId, boolean hasPermission, String code) {
        if(!hasPermission && !this.userId.equals(userId)) {
            throw new ResponseException(BadRequestError.UNABLE_TO_RESOLVE);
        }
        for (WalletCommand command : commands) {
            if (code.equals(command.getCode())) {
                if (!command.getCode().startsWith(BookingReadModel.BOOKING_PREFIX)) {
                    throw new ResponseException(BadRequestError.INVALID_ORDER_CODE);
                }
                if (!WalletCommandStatus.SUCCESS.equals(command.getStatus())) {
                    throw new ResponseException(BadRequestError.UNABLE_TO_RESOLVE);
                }
                command.returnSuccessOrder();
                WalletCommand successReturnCommand = new WalletCommand(command.getWalletId(), command.getAmount())
                        .resolve(null, WalletCommandStatus.WAIT_TO_PAY)
                        .success();
                this.plusMoney(this.from(successReturnCommand));
                this.commands.add(successReturnCommand);
                return successReturnCommand;
            }
        }
        throw new ResponseException(NotFoundError.WALLET_COMMAND_NOTFOUND);
    }

    public WalletCommand updateWithDrawCommand(UUID userId, UUID commandId, WithdrawCreateOrUpdateCmd cmd, Function<WalletCommand, String> createPaymentLinkFn) {
        this.checkWalletOwner(userId);
        for (WalletCommand command : commands) {
            if (commandId.equals(command.getId())) {
                command.createPaymentLink(createPaymentLinkFn.apply(command));
                return command.updateWithdrawCommand(cmd);
            }
        }
        throw new ResponseException(NotFoundError.WALLET_COMMAND_NOTFOUND);
    }

    public void deleteWithdrawCommand(UUID userId, UUID commandId) {
        this.checkWalletOwner(userId);
        for (WalletCommand command : commands) {
            if (commandId.equals(command.getId())) {
                command.delete();
                break;
            }
        }
        throw new ResponseException(NotFoundError.WALLET_COMMAND_NOTFOUND);
    }

    public WalletCommand resolveCommand(UUID commandId, UUID handlerId, WalletCommandStatus status, Function<WalletCommand, String> createPaymentLinkFn) {
        for (WalletCommand command : commands) {
            if (commandId.equals(command.getId())) {
                command.createPaymentLink(createPaymentLinkFn.apply(command));
                return command.resolve(handlerId, status);
            }
        }
        throw new ResponseException(NotFoundError.WALLET_COMMAND_NOTFOUND);
    }

    public WalletCommand doCommand(UUID commandId) {
        for (WalletCommand command : commands) {
            if (commandId.equals(command.getId())) {
                if (WalletAction.WITH_DRAW.equals(command.getAction())) {
                    this.minusMoney(this.from(command));
                }
                if (WalletAction.DEPOSIT.equals(command.getAction())) {
                    this.plusMoney(this.from(command));
                }
                return command.success();
            }
        }
        throw new ResponseException(NotFoundError.WALLET_COMMAND_NOTFOUND);
    }

    private void plusMoney(WalletHistoryCreateCmd command) {
        this.currentBalance += command.getAmount();
        this.histories.add(new WalletHistory(this.id, this.currentBalance, WalletAction.DEPOSIT, command));
    }

    private void minusMoney(WalletHistoryCreateCmd command) {
        if (command.getAmount() > this.currentBalance) {
            throw new ResponseException(BadRequestError.INSUFFICIENT_BALANCE);
        }
        this.currentBalance -= command.getAmount();
        this.histories.add(new WalletHistory(this.id, this.currentBalance, WalletAction.WITH_DRAW, command));
    }

    // utils
    public Wallet enrich() {
        return this.enrich(null);
    }

    public Wallet enrich(WalletCommand walletCommand) {
        this.histories = new ArrayList<>();
        this.commands = new ArrayList<>();
        if (Objects.nonNull(walletCommand)) {
            this.commands.add(walletCommand);
        }
        return this;
    }

    private void checkWalletOwner(UUID userId) {
        if (!this.userId.equals(userId)) {
            throw new ResponseException(BadRequestError.UNABLE_TO_CREATE_COMMAND);
        }
    }

    private WalletHistoryCreateCmd from(WalletCommand command, long amount) {
        return WalletHistoryCreateCmd.builder()
                .bankCode(command.getBankCode())
                .accountNumber(command.getAccountNumber())
                .receiverName(command.getReceiverName())
                .amount(amount)
                .content(command.getCode())
                .build();
    }

    private WalletHistoryCreateCmd from(WalletCommand command) {
        return this.from(command, command.getAmount());
    }
}
