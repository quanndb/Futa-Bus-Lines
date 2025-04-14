package com.fasfood.tripservice.presentation.rest.impl;

import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.tripservice.application.dto.request.TransitPointCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.request.TransitPointPagingRequest;
import com.fasfood.tripservice.application.dto.response.TransitPointDTO;
import com.fasfood.tripservice.application.service.cmd.TransitPointCommandService;
import com.fasfood.tripservice.application.service.query.TransitPointQueryService;
import com.fasfood.tripservice.presentation.rest.TransitPointController;
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
public class TransitPointControllerImpl implements TransitPointController {

    private final TransitPointCommandService cmdService;
    private final TransitPointQueryService queryService;

    @Override
    public ResponseEntity<byte[]> getTemplate() {
        byte[] template = this.queryService.getTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "transit-point-template.xlsx");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(template, headers, HttpStatus.OK);
    }

    @Override
    public Response<Void> upload(MultipartFile file) {
        this.cmdService.upload(file);
        return Response.ok();
    }

    @Override
    public Response<TransitPointDTO> create(TransitPointCreateOrUpdateRequest request) {
        return Response.of(this.cmdService.create(request));
    }

    @Override
    public Response<TransitPointDTO> update(UUID id, TransitPointCreateOrUpdateRequest request) {
        return Response.of(this.cmdService.update(id, request));
    }

    @Override
    public Response<Void> delete(UUID id) {
        this.cmdService.delete(id);
        return Response.ok();
    }

    @Override
    public PagingResponse<TransitPointDTO> getList(TransitPointPagingRequest request) {
        return PagingResponse.of(this.queryService.getList(request));
    }
}
