package com.fasfood.paymentservice.presentation.rest;

import com.fasfood.common.dto.request.PayRequest;
import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.paymentservice.application.dto.request.DepositRequest;
import com.fasfood.paymentservice.application.dto.request.WalletCommandPagingRequest;
import com.fasfood.paymentservice.application.dto.request.WithDrawCreateOrUpdateRequest;
import com.fasfood.paymentservice.application.dto.response.WalletCommandDTO;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletCommandStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Tag(name = "Wallet command resource")
@RequestMapping("/api/v1")
@Validated
public interface WalletCommandController {
    @Operation(summary = "Get my wallet commands")
    @GetMapping("/me/wallet-commands")
    PagingResponse<WalletCommandDTO> getMyWalletCommands(@ParameterObject WalletCommandPagingRequest request);

    @Operation(summary = "Get wallet commands")
    @GetMapping("/wallet-commands")
    @PreAuthorize("hasPermission(null, 'wallets.read')")
    PagingResponse<WalletCommandDTO> getWalletCommands(@ParameterObject WalletCommandPagingRequest request);

    @Operation(summary = "Create withdraw command")
    @PostMapping("/withdraws")
    Response<WalletCommandDTO> create(@RequestBody @Valid WithDrawCreateOrUpdateRequest request);

    @Operation(summary = "Create deposit command")
    @PostMapping("/deposits")
    Response<WalletCommandDTO> create(@RequestBody @Valid DepositRequest request);

    @Operation(summary = "Create payment")
    @PostMapping("/payments")
    @PreAuthorize("hasPermission(null, 'client.update')")
    Response<WalletCommandDTO> createPayment(@RequestBody @Valid PayRequest request);

    @Operation(summary = "Return payment")
    @PostMapping("/payments/return")
    Response<WalletCommandDTO> returnPayment(@RequestParam String code);

    @Operation(summary = "Update command")
    @PostMapping("/wallet-commands/{id}")
    Response<WalletCommandDTO> update(@PathVariable UUID id, @RequestBody @Valid WithDrawCreateOrUpdateRequest request);

    @Operation(summary = "Delete command")
    @DeleteMapping("/wallet-commands/{id}")
    Response<Void> delete(@PathVariable UUID id);

    @Operation(summary = "Resolve withdraw")
    @PatchMapping("/wallet-commands/{id}")
    @PreAuthorize("hasPermission(null, 'wallets.update')")
    Response<WalletCommandDTO> resolve(@PathVariable UUID id, @RequestParam WalletCommandStatus status);
}
