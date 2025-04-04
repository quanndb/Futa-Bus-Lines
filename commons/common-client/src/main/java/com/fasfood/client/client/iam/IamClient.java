package com.fasfood.client.client.iam;

import com.fasfood.client.config.FeignClientConfiguration;
import com.fasfood.common.UserAuthority;
import com.fasfood.common.dto.request.ClientRequest;
import com.fasfood.common.dto.response.ClientResponse;
import com.fasfood.common.dto.response.Response;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "iam",
        contextId = "iam",
        configuration = {FeignClientConfiguration.class},
        fallbackFactory = IamClientFallback.class
)
public interface IamClient {
    @GetMapping(value = {"/api/v1/accounts/{userId}/authorities"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    Response<UserAuthority> getUserAuthority(@PathVariable UUID userId);

    @PostMapping(value = "/api/v1/auth/client-token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Response<ClientResponse> getClientToken(@RequestBody @Valid ClientRequest clientRequest);
}
