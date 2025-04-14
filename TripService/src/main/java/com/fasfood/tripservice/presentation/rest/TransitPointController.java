package com.fasfood.tripservice.presentation.rest;

import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.tripservice.application.dto.request.TransitPointCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.request.TransitPointPagingRequest;
import com.fasfood.tripservice.application.dto.response.TransitPointDTO;
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

@Tag(name = "Transit point resource")
@RequestMapping("/api/v1/transit-points")
@Validated
public interface TransitPointController {
    @Operation(summary = "Get transit point excel template")
    @GetMapping("/template")
    @PreAuthorize("hasPermission(null, 'transit.read')")
    ResponseEntity<byte[]> getTemplate();

    @Operation(summary = "Upload transit points")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasPermission(null, 'transit.create')")
    Response<Void> upload(@RequestPart MultipartFile file);

    @Operation(summary = "Create transit point")
    @PostMapping(value = "")
    @PreAuthorize("hasPermission(null, 'transit.create')")
    Response<TransitPointDTO> create(@RequestBody @Valid TransitPointCreateOrUpdateRequest request);

    @Operation(summary = "Update transit point")
    @PostMapping(value = "/{id}")
    @PreAuthorize("hasPermission(null, 'transit.update')")
    Response<TransitPointDTO> update(@PathVariable UUID id, @RequestBody @Valid TransitPointCreateOrUpdateRequest request);

    @Operation(summary = "Delete transit point")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasPermission(null, 'transit.delete')")
    Response<Void> delete(@PathVariable UUID id);

    @Operation(summary = "Get transit points")
    @GetMapping(value = "")
    @PreAuthorize("hasPermission(null, 'transit.read')")
    PagingResponse<TransitPointDTO> getList(@ParameterObject TransitPointPagingRequest request);
}
