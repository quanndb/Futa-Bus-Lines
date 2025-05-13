package com.fasfood.tripservice.application.service.cmd;

import com.fasfood.common.cmd.CmdService;
import com.fasfood.tripservice.application.dto.request.TripCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.request.TripDetailsCreateRequest;
import com.fasfood.tripservice.application.dto.request.TripDetailsUpdateRequest;
import com.fasfood.tripservice.application.dto.request.TripTransitListCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.response.TripDTO;
import com.fasfood.tripservice.application.dto.response.TripDetailsDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface TripCommandService extends CmdService<TripDTO, TripCreateOrUpdateRequest, UUID> {

    TripDTO setTripTransits(UUID id, TripTransitListCreateOrUpdateRequest request);

    void upload(MultipartFile file);

    void upLoadTripDetails(MultipartFile file);

    TripDetailsDTO createDetails(UUID id, TripDetailsCreateRequest request);

    TripDetailsDTO updateDetails(UUID id, UUID detailsId, TripDetailsUpdateRequest request);

    void deleteDetails(UUID id, UUID detailsId);
}
