package com.fasfood.tripservice.presentation.rest.impl;

import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.tripservice.application.dto.request.PlaceCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.request.PlacePagingRequest;
import com.fasfood.tripservice.application.dto.response.PlaceDTO;
import com.fasfood.tripservice.application.service.cmd.PlaceCommandService;
import com.fasfood.tripservice.application.service.query.PlaceQueryService;
import com.fasfood.tripservice.presentation.rest.PlacementController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PlaceControllerImpl implements PlacementController {

    private final PlaceQueryService placeQueryService;
    private final PlaceCommandService placeCommandService;

    @Override
    public ResponseEntity<byte[]> getTemplate() {
        byte[] template = this.placeQueryService.getTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "place-template.xlsx");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(template, headers, HttpStatus.OK);
    }

    @Override
    public Response<Void> uploadPlace(MultipartFile file) {
        this.placeCommandService.upload(file);
        return Response.ok();
    }

    @Override
    public Response<PlaceDTO> createPlace(PlaceCreateOrUpdateRequest request) {
        return Response.of(this.placeCommandService.create(request));
    }

    @Override
    public Response<PlaceDTO> updatePlace(UUID id, PlaceCreateOrUpdateRequest request) {
        return Response.of(this.placeCommandService.update(id, request));
    }

    @Override
    public Response<Void> deletePlace(UUID id) {
        this.placeCommandService.delete(id);
        return Response.ok();
    }

    @Override
    public PagingResponse<PlaceDTO> getPlaces(PlacePagingRequest request) {
        return PagingResponse.of(this.placeQueryService.getList(request));
    }
}
