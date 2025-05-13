package com.fasfood.client.client.payment;

import com.fasfood.client.config.FeignClientConfiguration;
import com.fasfood.common.dto.request.PayRequest;
import com.fasfood.common.dto.response.PaymentLinkResponse;
import com.fasfood.common.dto.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        url = "${app.clients.payment:}",
        name = "payment",
        contextId = "payment",
        configuration = {FeignClientConfiguration.class},
        fallbackFactory = PaymentClientFallback.class
)
public interface PaymentClient {
    @PostMapping(value = "/api/v1/payment-links", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Response<PaymentLinkResponse> createPayment(@RequestBody PayRequest request);

    @PostMapping(value = "/api/v1/payment-links", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Response<PaymentLinkResponse> createPayment(@RequestBody PayRequest request, @RequestHeader("Authorization") String authorization);

    @PostMapping(value = "/api/v1/payment-links/return", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Response<Object> returnPayment(@RequestParam String code, @RequestHeader("Authorization") String authorization);

    @PostMapping(value = "/api/v1/payment-links/return", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Response<Object> returnPayment(@RequestParam String code);
}
