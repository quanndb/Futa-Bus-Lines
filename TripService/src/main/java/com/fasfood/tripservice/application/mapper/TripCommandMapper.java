package com.fasfood.tripservice.application.mapper;

import com.fasfood.common.mapper.CmdMapper;
import com.fasfood.tripservice.application.dto.request.TripCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.request.TripDetailsCreateRequest;
import com.fasfood.tripservice.application.dto.request.TripDetailsUpdateRequest;
import com.fasfood.tripservice.application.dto.request.TripTransitCreateOrUpdateRequest;
import com.fasfood.tripservice.domain.cmd.TripCreateOrUpdateCmd;
import com.fasfood.tripservice.domain.cmd.TripDetailsCreateOrUpdateCmd;
import com.fasfood.tripservice.domain.cmd.TripTransitCreateOrUpdateCmd;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TripCommandMapper extends CmdMapper<TripCreateOrUpdateCmd, TripCreateOrUpdateRequest> {
    TripDetailsCreateOrUpdateCmd from(TripDetailsCreateRequest request);

    TripDetailsCreateOrUpdateCmd from(TripDetailsUpdateRequest request);

    List<TripDetailsCreateOrUpdateCmd> fromDetails(List<TripDetailsCreateRequest> request);

    List<TripTransitCreateOrUpdateCmd> fromTransits(List<TripTransitCreateOrUpdateRequest> request);
}
