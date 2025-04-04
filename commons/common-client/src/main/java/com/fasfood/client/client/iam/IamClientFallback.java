package com.fasfood.client.client.iam;

import com.fasfood.common.UserAuthority;
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

import java.util.UUID;

@Component
@NoArgsConstructor
public class IamClientFallback implements FallbackFactory<IamClient> {

    @Override
    public IamClient create(Throwable cause) {
        return new FallbackWithFactory(cause);
    }

    static class FallbackWithFactory implements IamClient {
        private static final Logger log = LoggerFactory.getLogger(IamClientFallback.FallbackWithFactory.class);
        private final Throwable cause;

        FallbackWithFactory(Throwable cause) {
            this.cause = cause;
        }

        @Override
        public Response<UserAuthority> getUserAuthority(UUID userId) {
            log.error("Get user authorities", this.cause);
            return this.cause instanceof ForwardInnerAlertException ? Response.fail((RuntimeException) this.cause)
                    : Response.fail(new ResponseException(ServiceUnavailableError.SERVICE_UNAVAILABLE_ERROR));
        }

        @Override
        public Response<ClientResponse> getClientToken(ClientRequest clientRequest) {
            log.error("Get client token fail cause:", this.cause);
            return this.cause instanceof ForwardInnerAlertException ? Response.fail((RuntimeException) this.cause)
                    : Response.fail(new ResponseException(ServiceUnavailableError.SERVICE_UNAVAILABLE_ERROR));
        }
    }
}
