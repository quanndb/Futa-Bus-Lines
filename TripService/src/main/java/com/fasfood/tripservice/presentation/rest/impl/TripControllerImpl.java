package com.fasfood.tripservice.presentation.rest.impl;

import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.tripservice.application.dto.request.TripCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.request.TripDetailsCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.request.TripFilterRequest;
import com.fasfood.tripservice.application.dto.request.TripPagingRequest;
import com.fasfood.tripservice.application.dto.response.TripDTO;
import com.fasfood.tripservice.application.dto.response.TripDetailsDTO;
import com.fasfood.tripservice.application.dto.response.TripResponse;
import com.fasfood.tripservice.application.service.cmd.TripCommandService;
import com.fasfood.tripservice.application.service.query.TripQueryService;
import com.fasfood.tripservice.presentation.rest.TripController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TripControllerImpl implements TripController {

    private final TripCommandService commandService;
    private final TripQueryService queryService;

    @Override
    public ResponseEntity<byte[]> getTemplate() {
        byte[] template = this.queryService.getTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "trip.xlsx");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(template, headers, HttpStatus.OK);
    }

    @Override
    public Response<Void> upload(MultipartFile file) {
        this.commandService.upload(file);
        return Response.ok();
    }

    @Override
    public Response<TripDTO> create(TripCreateOrUpdateRequest request) {
        return Response.of(this.commandService.create(request));
    }

    @Override
    public Response<TripDTO> update(UUID id, TripCreateOrUpdateRequest request) {
        return Response.of(this.commandService.update(id, request));
    }

    @Override
    public Response<Void> delete(UUID id) {
        this.commandService.delete(id);
        return Response.ok();
    }

    @Override
    public PagingResponse<TripDTO> getList(TripPagingRequest request) {
        return PagingResponse.of(this.queryService.getList(request));
    }

    @Override
    public Response<List<TripResponse>> findTrip(TripFilterRequest request) {
        return Response.of(this.queryService.findTrips(request));
    }

    @Override
    public Response<TripDTO> getById(UUID id) {
        return Response.of(this.queryService.getById(id));
    }
}
