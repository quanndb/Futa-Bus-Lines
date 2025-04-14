package com.fasfood.tripservice.presentation.rest.impl;

import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.tripservice.application.dto.request.BusCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.request.BusPagingRequest;
import com.fasfood.tripservice.application.dto.response.BusDTO;
import com.fasfood.tripservice.application.service.cmd.BusCommandService;
import com.fasfood.tripservice.application.service.query.BusQueryService;
import com.fasfood.tripservice.presentation.rest.BusController;
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
public class BusControllerImpl implements BusController {

    private final BusQueryService busQueryService;
    private final BusCommandService busCommandService;

    @Override
    public ResponseEntity<byte[]> getTemplate() {
        byte[] template = this.busQueryService.getTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "bus-template.xlsx");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(template, headers, HttpStatus.OK);
    }

    @Override
    public Response<Void> upload(MultipartFile file) {
        this.busCommandService.upload(file);
        return Response.ok();
    }

    @Override
    public Response<BusDTO> create(BusCreateOrUpdateRequest request) {
        return Response.of(this.busCommandService.create(request));
    }

    @Override
    public Response<BusDTO> update(UUID id, BusCreateOrUpdateRequest request) {
        return Response.of(this.busCommandService.update(id, request));
    }

    @Override
    public Response<Void> delete(UUID id) {
        this.busCommandService.delete(id);
        return Response.ok();
    }

    @Override
    public PagingResponse<BusDTO> getBuses(BusPagingRequest request) {
        return PagingResponse.of(this.busQueryService.getList(request));
    }

    @Override
    public Response<BusDTO> getBus(UUID id) {
        return Response.of(this.busQueryService.getById(id));
    }
}
