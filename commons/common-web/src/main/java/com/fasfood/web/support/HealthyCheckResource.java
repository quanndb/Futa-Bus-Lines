package com.fasfood.web.support;

import com.fasfood.common.dto.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Healthy Check Resource"
)
@RestController
public class HealthyCheckResource {
    public HealthyCheckResource() {
    }

    @Operation(
            summary = "NR - Root path"
    )
    @GetMapping({""})
    Response<Void> check() {
        return Response.ok();
    }

    @Operation(
            summary = "NR - Check Health"
    )
    @GetMapping({"/health"})
    Response<Void> health() {
        return Response.ok();
    }
}
