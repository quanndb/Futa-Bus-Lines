package com.fasfood.tripservice.presentation.rest.impl;

import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.tripservice.application.dto.request.RouteCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.request.RoutePagingRequest;
import com.fasfood.tripservice.application.dto.response.RouteDTO;
import com.fasfood.tripservice.application.service.cmd.RouteCommandService;
import com.fasfood.tripservice.application.service.query.RouteQueryService;
import com.fasfood.tripservice.presentation.rest.RouteController;
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
public class RouteControllerImpl implements RouteController {

    private final RouteQueryService routeQueryService;
    private final RouteCommandService routeCommandService;

    @Override
    public ResponseEntity<byte[]> getTemplate() {
        byte[] template = this.routeQueryService.getTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "route-template.xlsx");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(template, headers, HttpStatus.OK);
    }

    @Override
    public Response<Void> uploadRoute(MultipartFile file) {
        this.routeCommandService.upload(file);
        return Response.ok();
    }

    @Override
    public Response<RouteDTO> createRoute(RouteCreateOrUpdateRequest request) {
        return Response.of(this.routeCommandService.create(request));
    }

    @Override
    public Response<RouteDTO> updateRoute(UUID id, RouteCreateOrUpdateRequest request) {
        return Response.of(this.routeCommandService.update(id, request));
    }

    @Override
    public Response<Void> deleteRoute(UUID id) {
        this.routeCommandService.delete(id);
        return Response.ok();
    }

    @Override
    public PagingResponse<RouteDTO> getRoute(RoutePagingRequest request) {
        return PagingResponse.of(this.routeQueryService.getList(request));
    }
}
