package com.fasfood.paymentservice.presentation.rest.impl;

import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.paymentservice.application.dto.request.DepositRequest;
import com.fasfood.common.dto.request.PayRequest;
import com.fasfood.paymentservice.application.dto.request.WalletCommandPagingRequest;
import com.fasfood.paymentservice.application.dto.request.WithDrawCreateOrUpdateRequest;
import com.fasfood.paymentservice.application.dto.response.WalletCommandDTO;
import com.fasfood.paymentservice.application.service.cmd.PaymentCommandService;
import com.fasfood.paymentservice.application.service.query.PaymentQueryService;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletCommandStatus;
import com.fasfood.paymentservice.presentation.rest.WalletCommandController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WalletCommandControllerImpl implements WalletCommandController {

    private final PaymentCommandService commandService;
    private final PaymentQueryService queryService;

    @Override
    public PagingResponse<WalletCommandDTO> getMyWalletCommands(WalletCommandPagingRequest request) {
        return PagingResponse.of(this.queryService.getMyWalletCommands(request));
    }

    @Override
    public PagingResponse<WalletCommandDTO> getWalletCommands(WalletCommandPagingRequest request) {
        return PagingResponse.of(this.queryService.getCommands(request));
    }

    @Override
    public Response<WalletCommandDTO> create(WithDrawCreateOrUpdateRequest request) {
        return Response.of(this.commandService.create(request));
    }

    @Override
    public Response<WalletCommandDTO> create(DepositRequest request) {
        return Response.of(this.commandService.create(request));
    }

    @Override
    public Response<WalletCommandDTO> createPayment(PayRequest request) {
        return Response.of(this.commandService.createPayment(request));
    }

    @Override
    public Response<WalletCommandDTO> returnPayment(String code) {
        return Response.of(this.commandService.returnPayment(code));
    }

    @Override
    public Response<WalletCommandDTO> update(UUID id, WithDrawCreateOrUpdateRequest request) {
        return Response.of(this.commandService.update(id, request));
    }

    @Override
    public Response<Void> delete(UUID id) {
        this.commandService.delete(id);
        return Response.ok();
    }

    @Override
    public Response<WalletCommandDTO> resolve(UUID id, WalletCommandStatus status) {
        return Response.of(this.commandService.resolve(id, status));
    }
}
