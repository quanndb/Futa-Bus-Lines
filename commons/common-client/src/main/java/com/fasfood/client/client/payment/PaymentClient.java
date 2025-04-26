package com.fasfood.client.client.payment;

import com.fasfood.client.config.FeignClientConfiguration;
import com.fasfood.common.dto.request.PayRequest;
import com.fasfood.common.dto.response.PaymentLinkResponse;
import com.fasfood.common.dto.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        url = "${app.clients.payment:}",
        name = "payment",
        contextId = "payment",
        configuration = {FeignClientConfiguration.class},
        fallbackFactory = PaymentClientFallback.class
)
public interface PaymentClient {
    @PostMapping("/api/v1/payments")
    Response<PaymentLinkResponse> createPayment(@RequestBody PayRequest request);

    @PostMapping("/api/v1/payments/return")
    Response<Object> returnPayment(@RequestParam String orderCode);
}
