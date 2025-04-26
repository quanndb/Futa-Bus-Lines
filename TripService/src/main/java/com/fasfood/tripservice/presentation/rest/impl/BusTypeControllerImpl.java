package com.fasfood.tripservice.presentation.rest.impl;

import com.fasfood.common.dto.response.Response;
import com.fasfood.tripservice.application.dto.request.BusTypeCreateOrUpdateRequest;
import com.fasfood.common.dto.response.BusTypeDTO;
import com.fasfood.tripservice.application.service.cmd.BusTypeCommandService;
import com.fasfood.tripservice.application.service.query.BusTypeQueryService;
import com.fasfood.tripservice.presentation.rest.BusTypeController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BusTypeControllerImpl implements BusTypeController {

    private final BusTypeQueryService queryService;
    private final BusTypeCommandService commandService;

    @Override
    public Response<BusTypeDTO> create(BusTypeCreateOrUpdateRequest request) {
        return Response.of(this.commandService.create(request));
    }

    @Override
    public Response<BusTypeDTO> update(UUID id, BusTypeCreateOrUpdateRequest request) {
        return Response.of(this.commandService.update(id, request));
    }

    @Override
    public Response<Void> delete(UUID id) {
        this.commandService.delete(id);
        return Response.ok();
    }

    @Override
    public Response<List<BusTypeDTO>> getAll() {
        return Response.of(this.queryService.getAll());
    }

    @Override
    public Response<BusTypeDTO> getById(UUID id) {
        return Response.of(this.queryService.getById(id));
    }
}
