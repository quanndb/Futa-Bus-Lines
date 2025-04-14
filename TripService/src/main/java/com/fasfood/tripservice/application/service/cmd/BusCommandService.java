package com.fasfood.tripservice.application.service.cmd;

import com.fasfood.common.cmd.CmdService;
import com.fasfood.tripservice.application.dto.request.BusCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.response.BusDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface BusCommandService extends CmdService<BusDTO, BusCreateOrUpdateRequest, UUID> {
    void upload(MultipartFile file);
}