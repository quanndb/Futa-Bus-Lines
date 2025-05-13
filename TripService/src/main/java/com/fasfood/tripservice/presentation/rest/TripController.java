package com.fasfood.tripservice.presentation.rest;

import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.common.dto.response.StatisticResponse;
import com.fasfood.tripservice.application.dto.request.TripCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.request.TripFilterRequest;
import com.fasfood.tripservice.application.dto.request.TripPagingRequest;
import com.fasfood.tripservice.application.dto.request.TripTransitListCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.response.TripDTO;
import com.fasfood.tripservice.application.dto.response.TripResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "Trip resource")
@RequestMapping("/api/v1/trips")
@Validated
public interface TripController {
    @Operation(summary = "Get trip excel template")
    @GetMapping("/template")
    @PreAuthorize("hasPermission(null, 'trip.read')")
    ResponseEntity<byte[]> getTemplate();

    @Operation(summary = "Upload trips")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasPermission(null, 'trip.create')")
    Response<Void> upload(@RequestPart MultipartFile file);

    @Operation(summary = "Create trip")
    @PostMapping(value = "")
    @PreAuthorize("hasPermission(null, 'trip.create')")
    Response<TripDTO> create(@RequestBody @Valid TripCreateOrUpdateRequest request);

    @Operation(summary = "Update trip")
    @PostMapping(value = "/{id}")
    @PreAuthorize("hasPermission(null, 'trip.update')")
    Response<TripDTO> update(@PathVariable UUID id, @RequestBody @Valid TripCreateOrUpdateRequest request);

    @Operation(summary = "Update trip")
    @PostMapping(value = "/{id}/transits")
    @PreAuthorize("hasPermission(null, 'trip.update')")
    Response<TripDTO> updateTripTransit(@PathVariable UUID id, @RequestBody @Valid TripTransitListCreateOrUpdateRequest request);

    @Operation(summary = "Delete trip")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasPermission(null, 'trip.delete')")
    Response<Void> delete(@PathVariable UUID id);

    @Operation(summary = "Get trips")
    @GetMapping(value = "")
    PagingResponse<TripDTO> getList(@ParameterObject TripPagingRequest request);

    @Operation(summary = "Get trips")
    @GetMapping(value = "/schedules")
    Response<List<TripResponse>> findTrip(@ParameterObject @Valid TripFilterRequest request);

    @Operation(summary = "Get trip by id")
    @GetMapping(value = "/{id}")
    Response<TripDTO> getById(@PathVariable UUID id);

    @Operation(summary = "Get trip statistics")
    @GetMapping(value = "/statistics")
    @PreAuthorize("hasPermission(null, 'trip.read')")
    Response<List<StatisticResponse>> getTripStatistics(@RequestParam(required = false) Integer year);
}
