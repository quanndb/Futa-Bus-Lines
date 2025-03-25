package com.fasfood.client.client.google;

import com.fasfood.common.dto.response.GoogleUserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

public class GoogleClientFallback implements FallbackFactory<GoogleClient> {
    @Override
    public GoogleClient create(Throwable cause) {
        return new FallbackWithFactory(cause);
    }

    static class FallbackWithFactory implements GoogleClient {
        private static final Logger log = LoggerFactory.getLogger(GoogleClientFallback.FallbackWithFactory.class);
        private final Throwable cause;

        FallbackWithFactory(Throwable cause) {
            this.cause = cause;
        }

        @Override
        public GoogleUserResponse getUserInfo(String authHeader) {
            log.error("Get google's user info", this.cause);
            return null;
        }
    }
}
