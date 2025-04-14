package com.fasfood.tripservice.application.service.cmd.impl;

import com.fasfood.common.exception.ResponseException;
import com.fasfood.commonexcel.CellValidator;
import com.fasfood.commonexcel.ExcelUtil;
import com.fasfood.tripservice.application.dto.mapper.BusDTOMapper;
import com.fasfood.tripservice.application.dto.request.BusCreateOrUpdateRequest;
import com.fasfood.tripservice.application.dto.response.BusDTO;
import com.fasfood.tripservice.application.mapper.BusCommandMapper;
import com.fasfood.tripservice.application.service.cmd.BusCommandService;
import com.fasfood.tripservice.domain.Bus;
import com.fasfood.tripservice.domain.cmd.BusCreateOrUpdateCmd;
import com.fasfood.tripservice.domain.repository.BusRepository;
import com.fasfood.tripservice.infrastructure.persistence.repository.BusEntityRepository;
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
public class BusCommandServiceImpl implements BusCommandService {

    private final BusCommandMapper busCommandMapper;
    private final BusRepository busRepository;
    private final BusEntityRepository busEntityRepository;
    private final BusDTOMapper busDTOMapper;

    @Override
    @Transactional
    public void upload(MultipartFile file) {
        List<BusCreateOrUpdateCmd> cmds = this.busCommandMapper
                .cmdListFromRequestList(this.extractBusRequestFromFile(file));
        List<Bus> buses = new ArrayList<>();
        for (BusCreateOrUpdateCmd cmd : cmds) {
            buses.add(new Bus(cmd));
        }
        this.busEntityRepository.deleteAll();
        this.busRepository.saveAll(buses);
    }

    @Override
    @Transactional
    public BusDTO create(BusCreateOrUpdateRequest request) {
        if (this.busEntityRepository.existsByLicensePlate(request.getLicensePlate())) {
            throw new ResponseException(BadRequestError.EXISTED_LICENSE_PLATE, request.getLicensePlate());
        }
        BusCreateOrUpdateCmd cmd = this.busCommandMapper.cmdFromRequest(request);
        return this.busDTOMapper.domainToDTO(this.busRepository.save(new Bus(cmd)));
    }

    @Override
    @Transactional
    public BusDTO update(UUID id, BusCreateOrUpdateRequest request) {
        Bus found = this.busRepository.getById(id);
        BusCreateOrUpdateCmd cmd = this.busCommandMapper.cmdFromRequest(request);
        return this.busDTOMapper.domainToDTO(this.busRepository.save(found.update(cmd)));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Bus found = this.busRepository.getById(id);
        found.delete();
        this.busRepository.save(found);
    }

    private List<BusCreateOrUpdateRequest> extractBusRequestFromFile(MultipartFile file) {
        // Create a mapper for the User class
        var mapper = ExcelExtractor.extractBus();
        // Add validation rules
        mapper.column("License plate").addValidationRule(CellValidator.notNull());
        mapper.column("Type").addValidationRule(CellValidator.notNull());

        // Import from file
        ExcelUtil.ImportResult<BusCreateOrUpdateRequest> result = mapper.importFromFile(file, "Buses", true);

        // Check for validation errors
        if (result.hasErrors()) {
            throw new ResponseException(BadRequestError.INVALID_FORMAT, result.getErrors());
        }

        List<BusCreateOrUpdateRequest> buses = result.getValidData();
        Set<String> seenLicensePlate = new HashSet<>();
        for (BusCreateOrUpdateRequest bus : buses) {
            String licensePlate = bus.getLicensePlate();
            if (licensePlate != null && !seenLicensePlate.add(licensePlate)) {
                throw new ResponseException(BadRequestError.EXISTED_CODE, licensePlate);
            }
        }
        return buses;
    }
}
