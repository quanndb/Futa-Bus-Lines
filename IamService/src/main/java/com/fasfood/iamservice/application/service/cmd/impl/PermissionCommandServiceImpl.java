package com.fasfood.iamservice.application.service.cmd.impl;

import com.fasfood.common.exception.ResponseException;
import com.fasfood.iamservice.application.dto.mapper.PermissionDTOMapper;
import com.fasfood.iamservice.application.dto.request.CreateOrUpdatePermissionRequest;
import com.fasfood.iamservice.application.dto.response.PermissionDTO;
import com.fasfood.iamservice.application.mapper.IamCommandMapper;
import com.fasfood.iamservice.application.service.cmd.PermissionCommandService;
import com.fasfood.iamservice.domain.Permission;
import com.fasfood.iamservice.domain.cmd.CreateOrUpdatePermissionCmd;
import com.fasfood.iamservice.domain.repository.PermissionRepository;
import com.fasfood.iamservice.infrastructure.persistence.entity.PermissionEntity;
import com.fasfood.iamservice.infrastructure.persistence.repository.PermissionEntityRepository;
import com.fasfood.iamservice.infrastructure.support.exception.BadRequestError;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionCommandServiceImpl implements PermissionCommandService {

    private final IamCommandMapper iamCommandMapper;
    private final PermissionDTOMapper dtoMapper;
    private final PermissionRepository permissionRepository;
    private final PermissionEntityRepository permissionEntityRepository;

    @Override
    @Transactional
    public PermissionDTO create(CreateOrUpdatePermissionRequest request) {
        this.checkExistedPermission(List.of(request.getCode()));
        CreateOrUpdatePermissionCmd cmd = this.iamCommandMapper.from(request);
        return this.dtoMapper.domainToDTO(this.permissionRepository.saveAll(List.of(new Permission(cmd)))).getFirst();
    }

    @Override
    @Transactional
    public PermissionDTO update(UUID id, CreateOrUpdatePermissionRequest request) {
        this.checkExistedPermission(List.of(request.getCode()));
        Permission found = this.permissionRepository.getById(id);
        CreateOrUpdatePermissionCmd cmd = this.iamCommandMapper.from(request);
        found.update(cmd);
        return this.dtoMapper.domainToDTO(this.permissionRepository.saveAll(List.of(found))).getFirst();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Permission found = this.permissionRepository.getById(id);
        found.delete();
        this.permissionRepository.saveAll(List.of(found));
    }

    private void checkExistedPermission(List<String> codes) {
        List<PermissionEntity> found = this.permissionEntityRepository.findExistedEntitiesByCodes(codes);
        if(!CollectionUtils.isEmpty(found)) {
            throw new ResponseException(BadRequestError.PERMISSION_EXISTED,
                    String.join(",", found.stream().map(PermissionEntity::getCode).toList()));
        }
    }
}
