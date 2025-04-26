package com.fasfood.tripservice.presentation.rest;

import com.fasfood.common.dto.response.Response;
import com.fasfood.tripservice.application.dto.request.BusTypeCreateOrUpdateRequest;
import com.fasfood.common.dto.response.BusTypeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Tag(name = "Bus types resource")
@RequestMapping("/api/v1/bus-types")
@Validated
public interface BusTypeController {

    @Operation(summary = "Create bus type")
    @PostMapping(value = "")
    @PreAuthorize("hasPermission(null, 'bus.create')")
    Response<BusTypeDTO> create(@RequestBody @Valid BusTypeCreateOrUpdateRequest request);

    @Operation(summary = "Update bus type")
    @PostMapping(value = "/{id}")
    @PreAuthorize("hasPermission(null, 'bus.update')")
    Response<BusTypeDTO> update(@PathVariable UUID id, @RequestBody @Valid BusTypeCreateOrUpdateRequest request);

    @Operation(summary = "Delete bus type")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasPermission(null, 'bus.delete')")
    Response<Void> delete(@PathVariable UUID id);

    @Operation(summary = "Get bus type")
    @GetMapping(value = "")
    Response<List<BusTypeDTO>> getAll();

    @Operation(summary = "Get bus type by id")
    @GetMapping(value = "/{id}")
    Response<BusTypeDTO> getById(@PathVariable UUID id);
}
