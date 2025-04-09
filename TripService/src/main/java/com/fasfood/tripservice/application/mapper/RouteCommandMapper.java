package com.fasfood.tripservice.application.mapper;

import com.fasfood.common.mapper.CmdMapper;
import com.fasfood.tripservice.application.dto.request.RouteCreateOrUpdateRequest;
import com.fasfood.tripservice.domain.cmd.RouteCreateOrUpDateCmd;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RouteCommandMapper extends CmdMapper<RouteCreateOrUpDateCmd, RouteCreateOrUpdateRequest> {
}
