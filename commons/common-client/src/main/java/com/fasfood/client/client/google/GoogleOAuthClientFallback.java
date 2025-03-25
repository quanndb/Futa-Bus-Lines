package com.fasfood.client.client.google;

import com.fasfood.common.dto.request.GetGoogleTokenRequest;
import com.fasfood.common.dto.response.GoogleTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

public class GoogleOAuthClientFallback implements FallbackFactory<GoogleOAuthClient> {
    @Override
    public GoogleOAuthClient create(Throwable cause) {
        return new FallbackWithFactory(cause);
    }

    static class FallbackWithFactory implements GoogleOAuthClient {
        private static final Logger log = LoggerFactory.getLogger(GoogleOAuthClientFallback.FallbackWithFactory.class);
        private final Throwable cause;

        FallbackWithFactory(Throwable cause) {
            this.cause = cause;
        }

        @Override
        public GoogleTokenResponse getToken(GetGoogleTokenRequest request) {
            log.error("Get google's token", this.cause);
            return null;
        }
    }
}
