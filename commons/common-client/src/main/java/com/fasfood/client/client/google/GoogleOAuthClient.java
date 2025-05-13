package com.fasfood.client.client.google;

import com.fasfood.client.config.FeignClientConfiguration;
import com.fasfood.common.dto.request.GetGoogleTokenRequest;
import com.fasfood.common.dto.response.GoogleTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        url = "${app.security.provider.google.verify-url:}",
        name = "google-oauth",
        contextId = "google-oauth",
        configuration = {FeignClientConfiguration.class},
        fallbackFactory = GoogleOAuthClientFallback.class
)
public interface GoogleOAuthClient {
    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    GoogleTokenResponse getToken(@SpringQueryMap GetGoogleTokenRequest request);
}
