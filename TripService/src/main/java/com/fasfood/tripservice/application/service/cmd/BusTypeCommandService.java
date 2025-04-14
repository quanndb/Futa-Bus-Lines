package com.fasfood.tripservice.application.service.cmd;

import com.fasfood.common.cmd.CmdService;
import com.fasfood.tripservice.application.dto.request.BusTypeCreateOrUpdateRequest;
import com.fasfood.common.dto.response.BusTypeDTO;

import java.util.UUID;

public interface BusTypeCommandService extends CmdService<BusTypeDTO, BusTypeCreateOrUpdateRequest, UUID> {
}
