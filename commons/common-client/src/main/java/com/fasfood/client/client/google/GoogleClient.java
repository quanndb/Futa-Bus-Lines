package com.fasfood.client.client.google;

import com.fasfood.client.config.FeignClientConfiguration;
import com.fasfood.common.dto.response.GoogleUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        url = "${app.security.provider.google.user-url}",
        name = "google-client",
        contextId = "google-client",
        configuration = {FeignClientConfiguration.class},
        fallbackFactory = GoogleClientFallback.class
)
public interface GoogleClient {
    @GetMapping("/userinfo")
    GoogleUserResponse getUserInfo(@RequestHeader(name = "Authorization") String authHeader);
}
