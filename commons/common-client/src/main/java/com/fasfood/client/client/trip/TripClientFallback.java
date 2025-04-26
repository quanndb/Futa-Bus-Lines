package com.fasfood.client.client.trip;

import com.fasfood.common.dto.response.BusTypeDTO;
import com.fasfood.common.dto.response.Response;
import com.fasfood.common.dto.response.TripDetailsResponse;
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
public class TripClientFallback implements FallbackFactory<TripClient> {
    @Override
    public TripClient create(Throwable cause) {
        return new TripClientFallback.FallbackWithFactory(cause);
    }

    static class FallbackWithFactory implements TripClient {
        private static final Logger log = LoggerFactory.getLogger(TripClientFallback.FallbackWithFactory.class);
        private final Throwable cause;

        FallbackWithFactory(Throwable cause) {
            this.cause = cause;
        }


        @Override
        public Response<BusTypeDTO> getById(UUID id) {
            log.error("Get bus type fails case:", this.cause);
            return this.cause instanceof ForwardInnerAlertException ? Response.fail((RuntimeException) this.cause)
                    : Response.fail(new ResponseException(ServiceUnavailableError.SERVICE_UNAVAILABLE_ERROR));
        }

        @Override
        public Response<TripDetailsResponse> getDetails(UUID id, UUID departureId, UUID arrivalId, String departureDate) {
            log.error("Get trip details fails case:", this.cause);
            return this.cause instanceof ForwardInnerAlertException ? Response.fail((RuntimeException) this.cause)
                    : Response.fail(new ResponseException(ServiceUnavailableError.SERVICE_UNAVAILABLE_ERROR));
        }
    }
}
