package com.fasfood.tripservice.application.service.cmd;

import com.fasfood.common.cmd.CmdService;
import com.fasfood.tripservice.application.dto.request.RouteCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.response.RouteDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface RouteCommandService extends CmdService<RouteDTO, RouteCreateOrUpdateRequest, UUID> {
    void upload(MultipartFile file);
}
