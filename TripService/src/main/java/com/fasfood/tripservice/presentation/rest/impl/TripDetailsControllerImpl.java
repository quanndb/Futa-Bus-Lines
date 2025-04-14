package com.fasfood.tripservice.presentation.rest.impl;

import com.fasfood.common.dto.response.Response;
import com.fasfood.tripservice.application.dto.request.TripDetailsCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.response.TripDetailsDTO;
import com.fasfood.tripservice.application.service.cmd.TripCommandService;
import com.fasfood.tripservice.application.service.query.TripQueryService;
import com.fasfood.tripservice.presentation.rest.TripDetailsController;
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
public class TripDetailsControllerImpl implements TripDetailsController {

    private final TripCommandService commandService;
    private final TripQueryService queryService;

    @Override
    public ResponseEntity<byte[]> getTemplate() {
        byte[] template = this.queryService.getTripDetailsTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "trip-details.xlsx");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(template, headers, HttpStatus.OK);
    }

    @Override
    public Response<Void> upload(MultipartFile file) {
        this.commandService.upLoadTripDetails(file);
        return Response.ok();
    }

    @Override
    public Response<List<TripDetailsDTO>> getTripDetails(UUID id) {
        return Response.of(this.queryService.tripDetails(id));
    }

    @Override
    public Response<TripDetailsDTO> createDetails(UUID id, TripDetailsCreateOrUpdateRequest request) {
        return Response.of(this.commandService.createDetails(id, request));
    }

    @Override
    public Response<TripDetailsDTO> updateTripDetails(UUID id, UUID detailsId, TripDetailsCreateOrUpdateRequest request) {
        return Response.of(this.commandService.updateDetails(id, detailsId, request));
    }

    @Override
    public Response<Void> deleteTripDetails(UUID id, UUID detailsId) {
        this.commandService.deleteDetails(id, detailsId);
        return Response.ok();
    }
}
