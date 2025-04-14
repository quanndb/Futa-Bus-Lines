package com.fasfood.tripservice.application.service.cmd;

import com.fasfood.common.cmd.CmdService;
import com.fasfood.tripservice.application.dto.request.TransitPointCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.response.TransitPointDTO;
import com.fasfood.tripservice.domain.TransitPoint;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TransitPointCommandService extends CmdService<TransitPointDTO, TransitPointCreateOrUpdateRequest, UUID> {
    void upload(MultipartFile file);

    List<TransitPoint> saveAll(Set<TransitPointCreateOrUpdateRequest> transitPointCreateOrUpdateRequests);
}
