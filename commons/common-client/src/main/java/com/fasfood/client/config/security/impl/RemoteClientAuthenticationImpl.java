package com.fasfood.client.config.security.impl;

import com.fasfood.client.client.keycloak.KeycloakClient;
import com.fasfood.client.config.security.ClientAuthentication;
import com.fasfood.common.dto.request.ClientRequest;
import com.fasfood.common.dto.response.ClientResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class RemoteClientAuthenticationImpl implements ClientAuthentication {

    private final KeycloakClient keycloakClient;

    public RemoteClientAuthenticationImpl(@Lazy KeycloakClient keycloakClient) {
        this.keycloakClient = keycloakClient;
    }

    @Override
    public ClientResponse getClientToken(ClientRequest clientRequest) {
        return this.keycloakClient.getClientToken(clientRequest).getData();
    }
}
