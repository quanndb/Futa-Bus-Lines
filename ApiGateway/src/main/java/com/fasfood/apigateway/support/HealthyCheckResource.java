package com.fasfood.apigateway.support;

import com.fasfood.apigateway.dto.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthyCheckResource {

    @GetMapping("/")
    Response<Void> check() {
        return Response.ok();
    }

    @GetMapping("/health")
    Response<Void> health() {
        return Response.ok();
    }
}
