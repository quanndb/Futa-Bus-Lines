package com.fasfood.iamservice.presentation.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Auth resource")
public interface JwkController {
    @Operation(summary = "Jwk certificate")
    @GetMapping("/certificate/.well-known/jwks.json")
    Map<String, Object> keys();
}
