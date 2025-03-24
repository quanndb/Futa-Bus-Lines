package com.fasfood.web.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;

import java.util.List;

public abstract class AbstractSwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return (new OpenAPI()).components((new Components())
                .addSecuritySchemes("bearerAuth", (new SecurityScheme())
                        .type(SecurityScheme.Type.HTTP).scheme("bearer")
                        .bearerFormat("JWT"))).security(List.of((new SecurityRequirement())
                .addList("bearerAuth"))).info(this.metadata());
    }

    protected abstract Info metadata();
}
