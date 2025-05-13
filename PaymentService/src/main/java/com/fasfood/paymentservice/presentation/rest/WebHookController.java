package com.fasfood.paymentservice.presentation.rest;

import com.fasfood.common.dto.request.WebhookRequest;
import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.common.dto.response.StatisticResponse;
import com.fasfood.paymentservice.application.dto.request.TransactionPagingRequest;
import com.fasfood.paymentservice.application.dto.response.TransactionDTO;
import com.fasfood.paymentservice.application.dto.response.WalletCommandDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Webhook resource")
@RequestMapping("/api/v1")
@Validated
public interface WebHookController {
    @Operation(summary = "Webhook withdraw received")
    @PostMapping("/withdraws/received")
    @PreAuthorize("hasPermission(null, 'client.update')")
    Response<WalletCommandDTO> withdraw(@RequestBody @Valid WebhookRequest request);

    @Operation(summary = "Webhook deposit received")
    @PostMapping("/deposits/received")
    @PreAuthorize("hasPermission(null, 'client.update')")
    Response<WalletCommandDTO> deposit(@RequestBody @Valid WebhookRequest request);

    @Operation(summary = "Get transactions")
    @GetMapping("/transactions")
    @PreAuthorize("hasPermission(null, 'transaction.read')")
    PagingResponse<TransactionDTO> getTransactions(@ParameterObject TransactionPagingRequest request);

    @Operation(summary = "Get transactions statistics")
    @GetMapping("/transactions/statistics")
    @PreAuthorize("hasPermission(null, 'transaction.read')")
    Response<StatisticResponse> getTransactionsStatistics(@ParameterObject TransactionPagingRequest request);
}
