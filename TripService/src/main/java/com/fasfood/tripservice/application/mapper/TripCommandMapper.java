package com.fasfood.tripservice.application.mapper;

import com.fasfood.common.mapper.CmdMapper;
import com.fasfood.tripservice.application.dto.request.TripCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.request.TripDetailsCreateOrUpdateRequest;
import com.fasfood.tripservice.domain.cmd.TripCreateOrUpdateCmd;
import com.fasfood.tripservice.domain.cmd.TripDetailsCreateOrUpdateCmd;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TripCommandMapper extends CmdMapper<TripCreateOrUpdateCmd, TripCreateOrUpdateRequest> {
    TripDetailsCreateOrUpdateCmd from(TripDetailsCreateOrUpdateRequest request);

    List<TripDetailsCreateOrUpdateCmd> from(List<TripDetailsCreateOrUpdateRequest> request);
}
