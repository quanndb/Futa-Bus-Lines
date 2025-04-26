package com.fasfood.paymentservice.presentation.rest;

import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.paymentservice.application.dto.request.WalletHistoryPagingRequest;
import com.fasfood.paymentservice.application.dto.response.WalletDTO;
import com.fasfood.paymentservice.application.dto.response.WalletHistoryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Wallet resource")
@RequestMapping("/api/v1")
@Validated
public interface WalletController {
    @Operation(summary = "Get my wallet")
    @GetMapping("/me/wallet")
    Response<WalletDTO> getMyWallet();

    @Operation(summary = "Get my wallet histories")
    @GetMapping("/me/wallet-histories")
    PagingResponse<WalletHistoryDTO> getMyHistories(@ParameterObject WalletHistoryPagingRequest request);

    @Operation(summary = "Get wallet histories")
    @GetMapping("/wallet-histories")
    @PreAuthorize("hasPermission(null, 'wallets.read')")
    PagingResponse<WalletHistoryDTO> getWalletHistories(@ParameterObject WalletHistoryPagingRequest request);
}
