package com.fasfood.tripservice.application.mapper;

import com.fasfood.common.mapper.CmdMapper;
import com.fasfood.tripservice.application.dto.request.TripCreateOrUpdateRequest;
import com.fasfood.tripservice.domain.cmd.TripCreateOrUpdateCmd;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TripCommandMapper extends CmdMapper<TripCreateOrUpdateCmd, TripCreateOrUpdateRequest> {
}
