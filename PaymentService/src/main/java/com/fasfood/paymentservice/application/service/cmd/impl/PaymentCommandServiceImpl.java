package com.fasfood.paymentservice.application.service.cmd.impl;

import com.fasfood.client.client.booking.BookingClient;
import com.fasfood.client.config.security.ClientAuthentication;
import com.fasfood.common.UserAuthentication;
import com.fasfood.common.constant.BookingReadModel;
import com.fasfood.common.dto.request.ClientRequest;
import com.fasfood.common.dto.request.PayRequest;
import com.fasfood.common.dto.request.WebhookRequest;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.paymentservice.application.dto.mapper.WalletCommandDTOMapper;
import com.fasfood.paymentservice.application.dto.request.DepositRequest;
import com.fasfood.paymentservice.application.dto.request.WithDrawCreateOrUpdateRequest;
import com.fasfood.paymentservice.application.dto.response.WalletCommandDTO;
import com.fasfood.paymentservice.application.mapper.PaymentCommandMapper;
import com.fasfood.paymentservice.application.service.cmd.PaymentCommandService;
import com.fasfood.paymentservice.domain.Transaction;
import com.fasfood.paymentservice.domain.Wallet;
import com.fasfood.paymentservice.domain.WalletCommand;
import com.fasfood.paymentservice.domain.cmd.DepositCmd;
import com.fasfood.paymentservice.domain.cmd.PayCmd;
import com.fasfood.paymentservice.domain.cmd.WithdrawCreateOrUpdateCmd;
import com.fasfood.paymentservice.domain.repository.TransactionRepository;
import com.fasfood.paymentservice.domain.repository.WalletRepository;
import com.fasfood.paymentservice.infrastructure.persistence.mapper.WalletCommandEntityMapper;
import com.fasfood.paymentservice.infrastructure.persistence.repository.WalletCommandEntityRepository;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletAction;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletCommandStatus;
import com.fasfood.paymentservice.infrastructure.support.exception.BadRequestError;
import com.fasfood.paymentservice.infrastructure.support.exception.NotFoundError;
import com.fasfood.paymentservice.infrastructure.support.readmodel.TransactionReadModel;
import com.fasfood.paymentservice.infrastructure.support.util.PaymentLinkCreator;
import com.fasfood.web.support.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties({ClientRequest.class})
public class PaymentCommandServiceImpl implements PaymentCommandService {

    private final WalletRepository walletRepository;
    private final PaymentCommandMapper commandMapper;
    private final WalletCommandDTOMapper walletCommandDTOMapper;
    private final WalletCommandEntityRepository walletCommandEntityRepository;
    private final WalletCommandEntityMapper walletCommandEntityMapper;
    private final TransactionRepository transactionRepository;
    private final PaymentLinkCreator paymentLinkCreator;
    private final BookingClient bookingClient;
    private final PermissionEvaluator permissionEvaluator;
    private final ClientAuthentication clientAuthentication;
    private final ClientRequest clientRequest;

    @Override
    @Transactional
    public WalletCommandDTO create(WithDrawCreateOrUpdateRequest request) {
        UserAuthentication authentication = SecurityUtils.authentication();
        Wallet found = this.walletRepository.getByUserId(authentication.getUserId());
        WithdrawCreateOrUpdateCmd cmd = this.commandMapper.cmdFromRequest(request);
        WalletCommand newCommand = found.createWithdrawCommand(authentication, cmd);
        this.walletRepository.save(found);
        return this.walletCommandDTOMapper.domainToDTO(newCommand);
    }

    @Override
    @Transactional
    public WalletCommandDTO create(DepositRequest request) {
        UserAuthentication authentication = SecurityUtils.authentication();
        Wallet found = this.walletRepository.getByUserId(authentication.getUserId());
        DepositCmd cmd = this.commandMapper.from(request);
        WalletCommand newCommand = found.createDepositCommand(authentication, cmd, this.createPaymentLinkFn());
        this.walletRepository.save(found);
        return this.walletCommandDTOMapper.domainToDTO(newCommand);
    }

    @Override
    @Transactional
    public WalletCommandDTO createPayment(PayRequest request) {
        this.walletCommandEntityRepository.findByCode(request.getCode())
                .ifPresent(item -> {
                    throw new ResponseException(BadRequestError.EXISTED_ORDER_CODE);
                });
        Wallet found = this.walletRepository.getByUserId(request.getUserId());
        PayCmd cmd = this.commandMapper.from(request);
        WalletCommand newCommand = found.createPayCommand(cmd, this.createPaymentLinkFn());
        if(WalletCommandStatus.SUCCESS.equals(newCommand.getStatus())) {
            this.bookingClient
                    .payBooking(newCommand.getCode(), String.join(" ", "Bearer", this.clientAuthentication.getClientToken(this.clientRequest).getAccessToken())).getData();
        }
        this.walletRepository.save(found);
        return this.walletCommandDTOMapper.domainToDTO(newCommand);
    }

    @Override
    @Transactional
    public WalletCommandDTO returnPayment(String orderCode) {
        boolean hasPermission = this.permissionEvaluator.hasPermission(SecurityUtils.authentication(), null, "client.update");
        WalletCommand foundCommand = this.walletCommandEntityMapper.toDomain(this.walletCommandEntityRepository
                .findByCode(orderCode).orElseThrow(() -> new ResponseException(NotFoundError.WALLET_COMMAND_NOTFOUND, orderCode)));
        Wallet found = this.walletRepository.getById(foundCommand.getWalletId()).enrich(foundCommand);
        WalletCommand successReturnCommand = found.createReturnCommand(SecurityUtils.getUserId(), hasPermission, orderCode);
        this.walletRepository.save(found);
        return this.walletCommandDTOMapper.domainToDTO(successReturnCommand);
    }

    @Override
    @Transactional
    public WalletCommandDTO update(UUID id, WithDrawCreateOrUpdateRequest request) {
        UUID userId = SecurityUtils.getUserId();
        Wallet found = this.walletRepository.getWalletByUserIdAndSelectedCommand(userId, id);
        WithdrawCreateOrUpdateCmd cmd = this.commandMapper.cmdFromRequest(request);
        WalletCommand updatedCommand = found.updateWithDrawCommand(userId, id, cmd, this.createPaymentLinkFn());
        this.walletRepository.save(found);
        return this.walletCommandDTOMapper.domainToDTO(updatedCommand);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        UUID userId = SecurityUtils.getUserId();
        Wallet found = this.walletRepository.getWalletByUserIdAndSelectedCommand(userId, id);
        found.deleteWithdrawCommand(userId, id);
        this.walletRepository.save(found);
    }

    @Override
    @Transactional
    public WalletCommandDTO resolve(UUID id, WalletCommandStatus status) {
        WalletCommand foundCommand = this.walletCommandEntityMapper.toDomain(this.walletCommandEntityRepository
                .findById(id).orElseThrow(() -> new ResponseException(NotFoundError.WALLET_COMMAND_NOTFOUND)));
        Wallet found = this.walletRepository.getById(foundCommand.getWalletId()).enrich(foundCommand);
        WalletCommand updatedCommand = found.resolveCommand(id, SecurityUtils.getUserId(), status, this.createPaymentLinkFn());
        this.walletRepository.save(found);
        return this.walletCommandDTOMapper.domainToDTO(updatedCommand);
    }

    @Override
    @Transactional
    public WalletCommandDTO deposit(WebhookRequest request) {
        this.transactionRepository.save(new Transaction(this.commandMapper.from(request)));
        if (Objects.nonNull(request.getCode())) {
            // deposit
            if (request.getCode().startsWith(TransactionReadModel.DEPOSIT_PREFIX)) {
                return this.doCommand(request.getCode(), request.getTransferAmount());
            }
            // pay the booking
            if (request.getCode().startsWith(BookingReadModel.BOOKING_PREFIX)) {
                var res = this.doCommand(request.getCode(), request.getTransferAmount());
                this.bookingClient.payBooking(request.getCode());
                return res;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public WalletCommandDTO withdraw(WebhookRequest request) {
        this.transactionRepository.save(new Transaction(this.commandMapper.from(request)));
        if (Objects.nonNull(request.getCode())) {
            // withdraw
            if (request.getCode().startsWith(TransactionReadModel.WITHDRAW_PREFIX)) {
                return this.doCommand(request.getCode(), request.getTransferAmount());
            }
        }
        return null;
    }

    private WalletCommandDTO doCommand(String code, long transferAmount) {
        WalletCommand foundCommand = this.walletCommandEntityMapper.toDomain(this.walletCommandEntityRepository
                .findByCode(code).orElseThrow(() -> new ResponseException(NotFoundError.WALLET_COMMAND_NOTFOUND, code)));
        if (!WalletAction.USING.equals(foundCommand.getAction())) {
            if (foundCommand.getAmount() != transferAmount) {
                throw new ResponseException(BadRequestError.INVALID_TRANSFER_AMOUNT);
            }
        }
        Wallet found = this.walletRepository.getById(foundCommand.getWalletId()).enrich(foundCommand);
        WalletCommand updatedCommand = found.doCommand(foundCommand.getId());
        this.walletRepository.save(found);
        return this.walletCommandDTOMapper.domainToDTO(updatedCommand);
    }

    private Function<WalletCommand, String> createPaymentLinkFn() {
        return this.paymentLinkCreator::create;
    }
}
