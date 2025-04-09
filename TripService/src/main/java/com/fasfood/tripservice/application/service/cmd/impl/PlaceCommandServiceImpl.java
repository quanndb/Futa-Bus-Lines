package com.fasfood.tripservice.application.service.cmd.impl;

import com.fasfood.common.exception.ResponseException;
import com.fasfood.commonexcel.CellValidator;
import com.fasfood.commonexcel.ExcelUtil;
import com.fasfood.tripservice.application.dto.mapper.PlaceDTOMapper;
import com.fasfood.tripservice.application.dto.request.PlaceCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.response.PlaceDTO;
import com.fasfood.tripservice.application.mapper.PlaceCommandMapper;
import com.fasfood.tripservice.application.service.cmd.PlaceCommandService;
import com.fasfood.tripservice.domain.Place;
import com.fasfood.tripservice.domain.cmd.PlaceCreateOrUpdateCmd;
import com.fasfood.tripservice.domain.repository.PlaceRepository;
import com.fasfood.tripservice.infrastructure.persistence.repository.PlaceEntityRepository;
import com.fasfood.tripservice.infrastructure.support.exception.BadRequestError;
import com.fasfood.tripservice.infrastructure.support.util.ExcelExtractor;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlaceCommandServiceImpl implements PlaceCommandService {

    private final PlaceRepository placeRepository;
    private final PlaceEntityRepository placeEntityRepository;
    private final PlaceCommandMapper placeCommandMapper;
    private final PlaceDTOMapper placeDTOMapper;

    @Override
    @Transactional
    public PlaceDTO create(PlaceCreateOrUpdateRequest request) {
        this.placeEntityRepository.findByCode(request.getCode())
                .ifPresent(placeEntity -> {
                    throw new ResponseException(BadRequestError.EXISTED_CODE, request.getCode());
                });
        PlaceCreateOrUpdateCmd cmd = this.placeCommandMapper.cmdFromRequest(request);
        return this.placeDTOMapper.domainToDTO(this.placeRepository.save(new Place(cmd)));
    }

    @Override
    @Transactional
    public PlaceDTO update(UUID id, PlaceCreateOrUpdateRequest request) {
        Place found = this.placeRepository.getById(id);
        this.placeEntityRepository.findByCodeExcept(request.getCode(), found.getCode())
                .ifPresent(placeEntity -> {
                    throw new ResponseException(BadRequestError.EXISTED_CODE, request.getCode());
                });
        found = found.update(this.placeCommandMapper.cmdFromRequest(request));
        return this.placeDTOMapper.domainToDTO(this.placeRepository.save(found));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Place found = this.placeRepository.getById(id);
        found.delete();
        this.placeRepository.save(found);
    }

    @Override
    @Transactional
    public void upload(MultipartFile file) {
        List<PlaceCreateOrUpdateCmd> cmds = this.placeCommandMapper
                .cmdListFromRequestList(this.extractPlaceRequestFromFile(file));
        List<Place> places = new ArrayList<>();
        for (PlaceCreateOrUpdateCmd cmd : cmds) {
            places.add(new Place(cmd));
        }
        this.placeEntityRepository.deleteAll();
        this.placeRepository.saveAll(places);
    }

    private List<PlaceCreateOrUpdateRequest> extractPlaceRequestFromFile(MultipartFile file) {
        // Create a mapper for the User class
        var mapper = ExcelExtractor.extractPlace();
        // Add validation rules
        mapper.column("Name").addValidationRule(CellValidator.notNull());
        mapper.column("Address").addValidationRule(CellValidator.notNull());
        mapper.column("Code").addValidationRule(CellValidator.notNull());

        // Import from file
        ExcelUtil.ImportResult<PlaceCreateOrUpdateRequest> result = mapper.importFromFile(file, "Places", true);

        // Check for validation errors
        if (result.hasErrors()) {
            throw new ResponseException(BadRequestError.INVALID_FORMAT, result.getErrors());
        }

        List<PlaceCreateOrUpdateRequest> places = result.getValidData();
        Set<String> seenCodes = new HashSet<>();
        for (PlaceCreateOrUpdateRequest place : places) {
            String code = place.getCode();
            if (code != null && !seenCodes.add(code)) {
                throw new ResponseException(BadRequestError.EXISTED_CODE, code);
            }
        }
        return places;
    }
}
