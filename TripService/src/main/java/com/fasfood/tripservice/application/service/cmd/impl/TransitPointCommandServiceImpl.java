package com.fasfood.tripservice.application.service.cmd.impl;

import com.fasfood.common.exception.ResponseException;
import com.fasfood.commonexcel.CellValidator;
import com.fasfood.commonexcel.ExcelUtil;
import com.fasfood.tripservice.application.dto.mapper.TransitPointDTOMapper;
import com.fasfood.tripservice.application.dto.request.TransitPointCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.response.TransitPointDTO;
import com.fasfood.tripservice.application.mapper.TransitPointCommandMapper;
import com.fasfood.tripservice.application.service.cmd.TransitPointCommandService;
import com.fasfood.tripservice.domain.TransitPoint;
import com.fasfood.tripservice.domain.cmd.TransitPointCreateOrUpdateCmd;
import com.fasfood.tripservice.domain.repository.TransitPointRepository;
import com.fasfood.tripservice.infrastructure.persistence.repository.TransitPointEntityRepository;
import com.fasfood.tripservice.infrastructure.support.exception.BadRequestError;
import com.fasfood.tripservice.infrastructure.support.util.ExcelExtractor;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransitPointCommandServiceImpl implements TransitPointCommandService {

    private final TransitPointEntityRepository transitPointEntityRepository;
    private final TransitPointRepository transitPointRepository;
    private final TransitPointCommandMapper transitPointCommandMapper;
    private final TransitPointDTOMapper transitPointDTOMapper;

    @Override
    @Transactional
    public void upload(MultipartFile file) {
        List<TransitPointCreateOrUpdateRequest> requests = this.extractTransitPointFromFile(file);
        this.saveAll(new HashSet<>(requests));
    }

    @Override
    @Transactional
    public List<TransitPoint> saveAll(Set<TransitPointCreateOrUpdateRequest> requests) {
        List<TransitPointCreateOrUpdateCmd> cmds = this.transitPointCommandMapper.cmdListFromRequestList(new ArrayList<>(requests));
        List<TransitPoint> transitPoints = new ArrayList<>();
        cmds.forEach(cmd -> transitPoints.add(new TransitPoint(cmd)));
        this.transitPointEntityRepository.deleteAll();
        return this.transitPointRepository.saveAll(transitPoints);
    }

    @Override
    @Transactional
    public TransitPointDTO create(TransitPointCreateOrUpdateRequest request) {
        this.transitPointEntityRepository.findByName(request.getName())
                .ifPresent((item) -> {
                    throw new ResponseException(BadRequestError.EXISTED_TRANSIT_NAME, request.getName());
                });
        TransitPointCreateOrUpdateCmd cmd = this.transitPointCommandMapper.cmdFromRequest(request);
        return this.transitPointDTOMapper.domainToDTO(this.transitPointRepository.save(new TransitPoint(cmd)));
    }

    @Override
    @Transactional
    public TransitPointDTO update(UUID id, TransitPointCreateOrUpdateRequest request) {
        TransitPoint found = this.transitPointRepository.getById(id);
        this.transitPointEntityRepository.findByNameExcept(request.getName(), found.getName())
                .ifPresent((item) -> {
                    throw new ResponseException(BadRequestError.EXISTED_TRANSIT_NAME, request.getName());
                });
        TransitPointCreateOrUpdateCmd cmd = this.transitPointCommandMapper.cmdFromRequest(request);
        return this.transitPointDTOMapper.domainToDTO(this.transitPointRepository.save(found.update(cmd)));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        TransitPoint found = this.transitPointRepository.getById(id);
        found.delete();
        this.transitPointRepository.save(found);
    }

    private List<TransitPointCreateOrUpdateRequest> extractTransitPointFromFile(MultipartFile file) {
        // Create a mapper for the User class
        var mapper = ExcelExtractor.extractTransitPoint();
        // Add validation rules
        mapper.column("Name").addValidationRule(CellValidator.notNull());
        mapper.column("Address").addValidationRule(CellValidator.notNull());
        mapper.column("Type (STATION/OFFICE/TRANSPORT)").addValidationRule(CellValidator.notNull());

        // Import from file
        ExcelUtil.ImportResult<TransitPointCreateOrUpdateRequest> result = mapper.importFromFile(file, "Transit points", true);

        // Check for validation errors
        if (result.hasErrors()) {
            throw new ResponseException(BadRequestError.INVALID_FORMAT, result.getErrors());
        }

        List<TransitPointCreateOrUpdateRequest> transitPoints = result.getValidData();
        Set<String> nameSet = new HashSet<>();
        List<String> errors = new ArrayList<>();
        for (TransitPointCreateOrUpdateRequest transitPoint : transitPoints) {
            String name = transitPoint.getName();
            if (name != null && !nameSet.add(name)) {
                errors.add("Duplicate name: " + name);
            }
        }
        if (!CollectionUtils.isEmpty(errors)) {
            throw new ResponseException(BadRequestError.INVALID_FORMAT, errors);
        }
        return transitPoints;
    }
}
