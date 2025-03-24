package com.fasfood.client.client.keycloak;

import com.fasfood.common.dto.request.ClientRequest;
import com.fasfood.common.dto.response.ClientResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.common.error.ServiceUnavailableError;
import com.fasfood.common.exception.ForwardInnerAlertException;
import com.fasfood.common.exception.ResponseException;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class KeycloakClientFallback implements FallbackFactory<KeycloakClient> {

    @Override
    public KeycloakClient create(Throwable cause) {
        return new FallbackWithFactory(cause);
    }

    static class FallbackWithFactory implements KeycloakClient {
        private static final Logger log = LoggerFactory.getLogger(FallbackWithFactory.class);
        private final Throwable cause;

        FallbackWithFactory(Throwable cause) {
            this.cause = cause;
        }

        @Override
        public Response<ClientResponse> getClientToken(ClientRequest clientRequest) {
            log.error("Get client token", this.cause);
            return this.cause instanceof ForwardInnerAlertException ? Response.fail((RuntimeException) this.cause)
                    : Response.fail(new ResponseException(ServiceUnavailableError.SERVICE_UNAVAILABLE_ERROR));
        }
    }
}
