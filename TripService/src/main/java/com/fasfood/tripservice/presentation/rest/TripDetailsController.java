package com.fasfood.tripservice.presentation.rest;

import com.fasfood.common.dto.response.Response;
import com.fasfood.common.dto.response.TripDetailsResponse;
import com.fasfood.tripservice.application.dto.request.TripDetailsCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.response.TripDetailsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Tag(name = "Trip details resource")
@RequestMapping("/api/v1")
@Validated
public interface TripDetailsController {

    @Operation(summary = "Get trip details excel template")
    @GetMapping("/template")
    @PreAuthorize("hasPermission(null, 'trip.read')")
    ResponseEntity<byte[]> getTemplate();

    @Operation(summary = "Upload trip details")
    @PostMapping(value = "/trip-details/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasPermission(null, 'trip.create')")
    Response<Void> upload(@RequestPart MultipartFile file);

    @Operation(summary = "Get trip details")
    @GetMapping(value = "/trips/{id}/details")
    @PreAuthorize("hasPermission(null, 'trip.read')")
    Response<List<TripDetailsDTO>> getTripDetails(@PathVariable UUID id);

    @Operation(summary = "Get trip details")
    @GetMapping(value = "/trips-details/{id}")
    Response<TripDetailsResponse> getDetails(@PathVariable UUID id, @RequestParam UUID departureId,
                                             @RequestParam UUID arrivalId, @RequestParam LocalDate departureDate);

    @Operation(summary = "Create trip details")
    @PostMapping(value = "/trips/{id}/details")
    @PreAuthorize("hasPermission(null, 'trip.create')")
    Response<TripDetailsDTO> createDetails(@PathVariable UUID id,
                                           @RequestBody @Valid TripDetailsCreateOrUpdateRequest request);

    @Operation(summary = "Update trip details")
    @PostMapping(value = "/{id}/details/{detailsId}")
    @PreAuthorize("hasPermission(null, 'trip.create')")
    Response<TripDetailsDTO> updateTripDetails(@PathVariable UUID id,
                                               @PathVariable UUID detailsId,
                                               @RequestBody @Valid TripDetailsCreateOrUpdateRequest request);

    @Operation(summary = "Delete trip details")
    @DeleteMapping(value = "/{id}/details/{detailsId}")
    @PreAuthorize("hasPermission(null, 'trip.delete')")
    Response<Void> deleteTripDetails(@PathVariable UUID id, @PathVariable UUID detailsId);
}
