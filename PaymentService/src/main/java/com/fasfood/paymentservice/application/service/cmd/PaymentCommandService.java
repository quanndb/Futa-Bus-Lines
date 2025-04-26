package com.fasfood.paymentservice.application.service.cmd;

import com.fasfood.common.cmd.CmdService;
import com.fasfood.common.dto.request.WebhookRequest;
import com.fasfood.paymentservice.application.dto.request.DepositRequest;
import com.fasfood.common.dto.request.PayRequest;
import com.fasfood.paymentservice.application.dto.request.WithDrawCreateOrUpdateRequest;
import com.fasfood.paymentservice.application.dto.response.WalletCommandDTO;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletCommandStatus;

import java.util.UUID;

public interface PaymentCommandService extends CmdService<WalletCommandDTO, WithDrawCreateOrUpdateRequest, UUID> {

    WalletCommandDTO create(DepositRequest request);

    WalletCommandDTO resolve(UUID id, WalletCommandStatus status);

    WalletCommandDTO deposit(WebhookRequest request);

    WalletCommandDTO withdraw(WebhookRequest request);

    WalletCommandDTO createPayment(PayRequest request);

    WalletCommandDTO returnPayment(String orderCode);
}
