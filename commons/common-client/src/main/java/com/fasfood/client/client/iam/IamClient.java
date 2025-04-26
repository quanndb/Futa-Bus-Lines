package com.fasfood.client.client.iam;

import com.fasfood.client.config.FeignClientConfiguration;
import com.fasfood.common.UserAuthority;
import com.fasfood.common.dto.request.PagingRequest;
import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.common.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        url = "${app.clients.iam:}",
        name = "iam",
        contextId = "iam",
        configuration = {FeignClientConfiguration.class},
        fallbackFactory = IamClientFallback.class
)
public interface IamClient {
    @GetMapping(value = {"/api/v1/accounts/{userId}/authorities"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    Response<UserAuthority> getUserAuthority(@PathVariable UUID userId);

    @GetMapping(value = "/api/v1/accounts", consumes = MediaType.APPLICATION_JSON_VALUE)
    PagingResponse<UserResponse> getUsersBysIds(@SpringQueryMap PagingRequest pagingRequest);
}
