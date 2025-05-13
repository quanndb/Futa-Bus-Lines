package com.fasfood.client.client.notification;

import com.fasfood.client.config.FeignClientConfiguration;
import com.fasfood.common.dto.request.SendEmailRequest;
import com.fasfood.common.dto.response.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        url = "${app.clients.notification:}",
        name = "notification",
        contextId = "notification",
        configuration = {FeignClientConfiguration.class},
        fallbackFactory = NotificationClientFallback.class
)
public interface NotificationClient {
    @PostMapping(value = "/api/v1/emails")
    Response<Void> send(@RequestBody @Valid SendEmailRequest sendEmailRequest) throws JsonProcessingException;

    @PostMapping(value = "/api/v1/emails")
    Response<Void> send(@RequestBody @Valid SendEmailRequest sendEmailRequest, @RequestHeader("Authorization") String authorization) throws JsonProcessingException;
}
