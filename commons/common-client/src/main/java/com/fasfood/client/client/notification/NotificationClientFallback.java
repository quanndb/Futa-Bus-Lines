package com.fasfood.client.client.notification;

import com.fasfood.common.dto.request.SendEmailRequest;
import com.fasfood.common.dto.response.Response;
import com.fasfood.common.error.ServiceUnavailableError;
import com.fasfood.common.exception.ForwardInnerAlertException;
import com.fasfood.common.exception.ResponseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class NotificationClientFallback implements FallbackFactory<NotificationClient> {
    @Override
    public NotificationClient create(Throwable cause) {
        return new FallbackWithFactory(cause);
    }

    static class FallbackWithFactory implements NotificationClient {
        private static final Logger log = LoggerFactory.getLogger(FallbackWithFactory.class);
        private final Throwable cause;

        FallbackWithFactory(Throwable cause) {
            this.cause = cause;
        }

        @Override
        public Response<Void> send(SendEmailRequest sendEmailRequest) {
            log.error("Send email fail cause: ", this.cause);
            return this.cause instanceof ForwardInnerAlertException ? Response.fail((RuntimeException) this.cause)
                    : Response.fail(new ResponseException(ServiceUnavailableError.SERVICE_UNAVAILABLE_ERROR));
        }

        @Override
        public Response<Void> send(SendEmailRequest sendEmailRequest, String authorization) throws JsonProcessingException {
            log.error("Send email fail cause: ", this.cause);
            return this.cause instanceof ForwardInnerAlertException ? Response.fail((RuntimeException) this.cause)
                    : Response.fail(new ResponseException(ServiceUnavailableError.SERVICE_UNAVAILABLE_ERROR));
        }
    }
}
