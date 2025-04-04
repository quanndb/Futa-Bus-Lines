package com.fasfood.client.config.security.impl;

import com.fasfood.client.client.iam.IamClient;
import com.fasfood.client.config.security.ClientAuthentication;
import com.fasfood.common.dto.request.ClientRequest;
import com.fasfood.common.dto.response.ClientResponse;
import com.fasfood.common.dto.response.Response;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RemoteClientAuthenticationImpl implements ClientAuthentication {

    private final IamClient iamClient;

    public RemoteClientAuthenticationImpl(@Lazy IamClient iamClient) {
        this.iamClient = iamClient;
    }

    @Override
    public ClientResponse getClientToken(ClientRequest clientRequest) {
        Response<ClientResponse> clientTokenResponse = this.iamClient.getClientToken(clientRequest);
        return Objects.nonNull(clientTokenResponse)
                && Objects.nonNull(clientTokenResponse.getData()) ? clientTokenResponse.getData() : null;
    }
}
