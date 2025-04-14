package com.fasfood.iamservice.application.service;

import com.fasfood.client.config.security.ClientAuthentication;
import com.fasfood.common.dto.request.ClientRequest;
import com.fasfood.common.dto.response.ClientResponse;
import com.fasfood.common.error.InternalServerError;
import com.fasfood.common.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class ClientService implements ClientAuthentication {

    @Value("${app.client.keycloak}")
    private String keycloakAuthURL;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public ClientResponse getClientToken(ClientRequest clientRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientRequest.getClientId(), clientRequest.getClientSecret()); // Basic Auth
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                String.join("", this.keycloakAuthURL, "/protocol/openid-connect/token"), HttpMethod.POST, request, Map.class
        );
        if (Objects.nonNull(response.getBody()) && Objects.nonNull(response.getBody().get("access_token"))) {
            return ClientResponse.builder().accessToken(response.getBody().get("access_token").toString()).build();
        }
        throw new ResponseException(InternalServerError.INTERNAL_SERVER_ERROR);
    }
}
