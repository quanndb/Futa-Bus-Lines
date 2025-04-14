package com.fasfood.tripservice.presentation.rest;

import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.tripservice.application.dto.request.BusCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.request.BusPagingRequest;
import com.fasfood.tripservice.application.dto.response.BusDTO;
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

@Tag(name = "Bus resource")
@RequestMapping("/api/v1/buses")
@Validated
public interface BusController {
    @Operation(summary = "Get bus excel template")
    @GetMapping("/template")
    @PreAuthorize("hasPermission(null, 'bus.read')")
    ResponseEntity<byte[]> getTemplate();

    @Operation(summary = "Upload buses")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasPermission(null, 'bus.create')")
    Response<Void> upload(@RequestPart MultipartFile file);

    @Operation(summary = "Create bus")
    @PostMapping(value = "")
    @PreAuthorize("hasPermission(null, 'bus.create')")
    Response<BusDTO> create(@RequestBody @Valid BusCreateOrUpdateRequest request);

    @Operation(summary = "Update bus")
    @PostMapping(value = "/{id}")
    @PreAuthorize("hasPermission(null, 'bus.update')")
    Response<BusDTO> update(@PathVariable UUID id, @RequestBody @Valid BusCreateOrUpdateRequest request);

    @Operation(summary = "Delete bus")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasPermission(null, 'bus.delete')")
    Response<Void> delete(@PathVariable UUID id);

    @Operation(summary = "Get buses")
    @GetMapping(value = "")
    @PreAuthorize("hasPermission(null, 'bus.read')")
    PagingResponse<BusDTO> getBuses(@ParameterObject BusPagingRequest request);

    @Operation(summary = "Get bus by id")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasPermission(null, 'bus.read')")
    Response<BusDTO> getBus(@PathVariable UUID id);
}
