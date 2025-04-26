package com.fasfood.paymentservice.presentation.rest.impl;

import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.paymentservice.application.dto.request.WalletHistoryPagingRequest;
import com.fasfood.paymentservice.application.dto.response.WalletDTO;
import com.fasfood.paymentservice.application.dto.response.WalletHistoryDTO;
import com.fasfood.paymentservice.application.service.cmd.PaymentCommandService;
import com.fasfood.paymentservice.application.service.query.PaymentQueryService;
import com.fasfood.paymentservice.presentation.rest.WalletController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WalletControllerImpl implements WalletController {

    private final PaymentQueryService queryService;
    private final PaymentCommandService commandService;

    @Override
    public Response<WalletDTO> getMyWallet() {
        return Response.of(this.queryService.getMyWallet());
    }

    @Override
    public PagingResponse<WalletHistoryDTO> getMyHistories(WalletHistoryPagingRequest request) {
        return PagingResponse.of(this.queryService.getMyWalletHistories(request));
    }

    @Override
    public PagingResponse<WalletHistoryDTO> getWalletHistories(WalletHistoryPagingRequest request) {
        return PagingResponse.of(this.queryService.getHistories(request));
    }
}
