package com.fasfood.paymentservice.presentation.rest.impl;

import com.fasfood.common.dto.request.WebhookRequest;
import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.common.dto.response.StatisticResponse;
import com.fasfood.paymentservice.application.dto.request.TransactionPagingRequest;
import com.fasfood.paymentservice.application.dto.response.TransactionDTO;
import com.fasfood.paymentservice.application.dto.response.WalletCommandDTO;
import com.fasfood.paymentservice.application.service.cmd.PaymentCommandService;
import com.fasfood.paymentservice.application.service.query.PaymentQueryService;
import com.fasfood.paymentservice.presentation.rest.WebHookController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebHookControllerImpl implements WebHookController {

    private final PaymentCommandService commandService;
    private final PaymentQueryService queryService;

    @Override
    public PagingResponse<TransactionDTO> getTransactions(TransactionPagingRequest request) {
        return PagingResponse.of(this.queryService.getTransactions(request));
    }

    @Override
    public Response<StatisticResponse> getTransactionsStatistics(TransactionPagingRequest request) {
        return PagingResponse.of(this.queryService.getStatistic(request));
    }

    @Override
    public ResponseEntity<byte[]> getTransactionsOutExcel(TransactionPagingRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "transaction-outs-"+request.getStartDate()+"-to-"+request.getEndDate()+".xlsx");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(this.queryService.getTransactionsOutExcel(request), headers, HttpStatus.OK);
    }

    @Override
    public Response<WalletCommandDTO> withdraw(WebhookRequest request) {
        return Response.of(this.commandService.withdraw(request));
    }

    @Override
    public Response<WalletCommandDTO> deposit(WebhookRequest request) {
        return Response.of(this.commandService.deposit(request));
    }
}
