package com.fasfood.client.client.keycloak;

import com.fasfood.client.config.FeignClientConfiguration;
import com.fasfood.common.dto.request.ClientRequest;
import com.fasfood.common.dto.response.ClientResponse;
import com.fasfood.common.dto.response.Response;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        url = "${app.client.keycloak}",
        name = "keycloak",
        contextId = "keycloak",
        configuration = {FeignClientConfiguration.class},
        fallbackFactory = KeycloakClientFallback.class
)
public interface KeycloakClient {
    @PostMapping(value = "/protocol/openid-connect/token",
            produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Response<ClientResponse> getClientToken(@QueryMap ClientRequest request);
}
