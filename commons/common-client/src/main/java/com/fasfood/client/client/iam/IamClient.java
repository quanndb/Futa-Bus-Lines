package com.fasfood.client.client.iam;

import com.fasfood.client.config.FeignClientConfiguration;
import com.fasfood.common.UserAuthority;
import com.fasfood.common.dto.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "iam",
        contextId = "iam",
        configuration = {FeignClientConfiguration.class},
        fallbackFactory = IamClientFallback.class
)
public interface IamClient {
    @GetMapping({"/api/v1/accounts/{userId}/authorities"})
    Response<UserAuthority> getUserAuthority(@PathVariable UUID userId);
}
