package com.fasfood.tripservice.application.mapper;

import com.fasfood.common.mapper.CmdMapper;
import com.fasfood.tripservice.application.dto.request.TransitPointCreateOrUpdateRequest;
import com.fasfood.tripservice.domain.cmd.TransitPointCreateOrUpdateCmd;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransitPointCommandMapper extends CmdMapper<TransitPointCreateOrUpdateCmd, TransitPointCreateOrUpdateRequest> {
}
