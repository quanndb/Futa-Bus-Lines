package com.fasfood.client.config.security.impl;

import com.fasfood.client.config.security.ClientAuthentication;
import com.fasfood.common.dto.request.ClientRequest;
import com.fasfood.common.dto.response.ClientResponse;
import com.fasfood.common.error.InternalServerError;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.client.support.DataCacher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;

@Component
public class RemoteClientAuthenticationImpl implements ClientAuthentication {

    @Value("${app.client.keycloak}")
    private String keycloakAuthURL;

    private final RestTemplate restTemplate;
    private final DataCacher dataCacher;

    public RemoteClientAuthenticationImpl(DataCacher dataCacher) {
        this.dataCacher = dataCacher;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public ClientResponse getClientToken(ClientRequest clientRequest) {
        if (this.dataCacher.hasKey(this.getKey(clientRequest))) {
            return ClientResponse.builder().accessToken(this.dataCacher.get(this.getKey(clientRequest), String.class)).build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientRequest.getClientId(), clientRequest.getClientSecret()); // Basic Auth
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                String.join("", this.keycloakAuthURL, "/protocol/openid-connect/token"), HttpMethod.POST, request, Map.class
        );
        if (Objects.nonNull(response.getBody()) && Objects.nonNull(response.getBody().get("access_token"))) {
            String accessToken = response.getBody().get("access_token").toString();
            this.dataCacher.put(this.getKey(clientRequest), accessToken, Duration.ofMinutes(5));
            return ClientResponse.builder().accessToken(accessToken).build();
        }
        throw new ResponseException(InternalServerError.INTERNAL_SERVER_ERROR);
    }

    private String getKey(ClientRequest clientRequest) {
        return String.join(":", clientRequest.getClientId(), clientRequest.getClientSecret());
    }
}
