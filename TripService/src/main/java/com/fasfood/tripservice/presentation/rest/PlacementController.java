package com.fasfood.tripservice.presentation.rest;

import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.tripservice.application.dto.request.PlaceCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.request.PlacePagingRequest;
import com.fasfood.tripservice.application.dto.response.PlaceDTO;
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

@Tag(name = "Place resource")
@RequestMapping("/api/v1/places")
@Validated
public interface PlacementController {
    @Operation(summary = "Get place excel template")
    @GetMapping("/template")
    @PreAuthorize("hasPermission(null, 'place.read')")
    ResponseEntity<byte[]> getTemplate();

    @Operation(summary = "Upload places")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasPermission(null, 'place.create')")
    Response<Void> uploadPlace(@RequestPart MultipartFile file);

    @Operation(summary = "Create place")
    @PostMapping(value = "")
    @PreAuthorize("hasPermission(null, 'place.create')")
    Response<PlaceDTO> createPlace(@RequestBody @Valid PlaceCreateOrUpdateRequest request);

    @Operation(summary = "Update place")
    @PostMapping(value = "/{id}")
    @PreAuthorize("hasPermission(null, 'place.update')")
    Response<PlaceDTO> updatePlace(@PathVariable UUID id, @RequestBody @Valid PlaceCreateOrUpdateRequest request);

    @Operation(summary = "Delete place")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasPermission(null, 'place.delete')")
    Response<Void> deletePlace(@PathVariable UUID id);

    @Operation(summary = "Get places")
    @GetMapping(value = "")
    PagingResponse<PlaceDTO> getPlaces(@ParameterObject PlacePagingRequest request);
}
