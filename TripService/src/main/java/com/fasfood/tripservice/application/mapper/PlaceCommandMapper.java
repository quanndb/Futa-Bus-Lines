package com.fasfood.tripservice.application.mapper;

import com.fasfood.common.mapper.CmdMapper;
import com.fasfood.tripservice.application.dto.request.PlaceCreateOrUpdateRequest;
import com.fasfood.tripservice.domain.cmd.PlaceCreateOrUpdateCmd;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlaceCommandMapper extends CmdMapper<PlaceCreateOrUpdateCmd, PlaceCreateOrUpdateRequest> {
}
