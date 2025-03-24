package com.fasfood.client.config.security;

import com.fasfood.common.dto.request.ClientRequest;
import com.fasfood.common.dto.response.ClientResponse;

public interface ClientAuthentication {
    ClientResponse getClientToken(ClientRequest clientRequest);
}
