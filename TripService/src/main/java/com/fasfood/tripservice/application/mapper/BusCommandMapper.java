package com.fasfood.tripservice.application.mapper;

import com.fasfood.common.mapper.CmdMapper;
import com.fasfood.tripservice.application.dto.request.BusCreateOrUpdateRequest;
import com.fasfood.tripservice.domain.cmd.BusCreateOrUpdateCmd;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusCommandMapper extends CmdMapper<BusCreateOrUpdateCmd, BusCreateOrUpdateRequest> {
}
