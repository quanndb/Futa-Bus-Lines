package com.fasfood.tripservice.presentation.rest;

import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.tripservice.application.dto.request.RouteCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.request.RoutePagingRequest;
import com.fasfood.tripservice.application.dto.response.RouteDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "Route resource")
@RequestMapping("/api/v1/routes")
@Validated
public interface RouteController {
    @Operation(summary = "Get route excel template")
    @GetMapping("/template")
    @PreAuthorize("hasPermission(null, 'route.read')")
    ResponseEntity<byte[]> getTemplate();

    @Operation(summary = "Upload routes")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasPermission(null, 'route.create')")
    Response<Void> uploadRoute(@RequestPart MultipartFile file);

    @Operation(summary = "Create route")
    @PostMapping(value = "")
    @PreAuthorize("hasPermission(null, 'route.create')")
    Response<RouteDTO> createRoute(@RequestBody @Valid RouteCreateOrUpdateRequest request);

    @Operation(summary = "Update route")
    @PostMapping(value = "/{id}")
    @PreAuthorize("hasPermission(null, 'route.update')")
    Response<RouteDTO> updateRoute(@PathVariable UUID id, @RequestBody @Valid RouteCreateOrUpdateRequest request);

    @Operation(summary = "Delete route")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasPermission(null, 'route.delete')")
    Response<Void> deleteRoute(@PathVariable UUID id);

    @Operation(summary = "Get routes")
    @GetMapping(value = "")
    PagingResponse<RouteDTO> getRoute(@ParameterObject RoutePagingRequest request);
}
