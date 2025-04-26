package com.fasfood.client.client.booking;

import com.fasfood.common.dto.request.GetBookedRequest;
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
import java.util.Map;
import java.util.UUID;

@Component
@NoArgsConstructor
public class BookingClientFallback implements FallbackFactory<BookingClient> {
    @Override
    public BookingClient create(Throwable cause) {
        return new BookingClientFallback.FallbackWithFactory(cause);
    }

    static class FallbackWithFactory implements BookingClient {
        private static final Logger log = LoggerFactory.getLogger(BookingClientFallback.FallbackWithFactory.class);
        private final Throwable cause;

        FallbackWithFactory(Throwable cause) {
            this.cause = cause;
        }

        @Override
        public Response<Map<UUID, List<String>>> getBooked(GetBookedRequest request) {
            log.error("Get booked fail case:", this.cause);
            return this.cause instanceof ForwardInnerAlertException ? Response.fail((RuntimeException) this.cause)
                    : Response.fail(new ResponseException(ServiceUnavailableError.SERVICE_UNAVAILABLE_ERROR));
        }

        @Override
        public Response<Void> payBooking(String code) {
            log.error("Notify payed fail case:", this.cause);
            return this.cause instanceof ForwardInnerAlertException ? Response.fail((RuntimeException) this.cause)
                    : Response.fail(new ResponseException(ServiceUnavailableError.SERVICE_UNAVAILABLE_ERROR));
        }
    }
}
