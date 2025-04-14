package com.fasfood.client.client.trip;

import com.fasfood.common.dto.response.BusTypeDTO;
import com.fasfood.common.dto.response.Response;
import com.fasfood.common.error.ServiceUnavailableError;
import com.fasfood.common.exception.ForwardInnerAlertException;
import com.fasfood.common.exception.ResponseException;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

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
        public Response<List<BusTypeDTO>> getAll() {
            log.error("Get bus types fails case:", this.cause);
            return this.cause instanceof ForwardInnerAlertException ? Response.fail((RuntimeException) this.cause)
                    : Response.fail(new ResponseException(ServiceUnavailableError.SERVICE_UNAVAILABLE_ERROR));
        }
    }
}
