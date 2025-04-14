package com.fasfood.tripservice.application.mapper;

import com.fasfood.common.mapper.CmdMapper;
import com.fasfood.tripservice.application.dto.request.BusTypeCreateOrUpdateRequest;
import com.fasfood.tripservice.domain.cmd.BusTypeCreateOrUpdateCmd;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusTypeCommandMapper extends CmdMapper<BusTypeCreateOrUpdateCmd, BusTypeCreateOrUpdateRequest> {
}
