package com.fasfood.tripservice.application.service.cmd;

import com.fasfood.common.cmd.CmdService;
import com.fasfood.tripservice.application.dto.request.PlaceCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.response.PlaceDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface PlaceCommandService extends CmdService<PlaceDTO, PlaceCreateOrUpdateRequest, UUID>{

    void upload(MultipartFile file);
}
