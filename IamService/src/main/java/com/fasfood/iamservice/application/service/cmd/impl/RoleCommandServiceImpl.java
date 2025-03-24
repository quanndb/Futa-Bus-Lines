package com.fasfood.iamservice.application.service.cmd.impl;

import com.fasfood.common.exception.ResponseException;
import com.fasfood.iamservice.application.dto.mapper.RoleDTOMapper;
import com.fasfood.iamservice.application.dto.request.AssignOrUnassignRolePermissionRequest;
import com.fasfood.iamservice.application.dto.request.CreateOrUpdateRoleRequest;
import com.fasfood.iamservice.application.dto.response.RoleDTO;
import com.fasfood.iamservice.application.mapper.IamCommandMapper;
import com.fasfood.iamservice.application.service.cmd.RoleCommandService;
import com.fasfood.iamservice.domain.Role;
import com.fasfood.iamservice.domain.cmd.CreateOrUpdateRoleCmd;
import com.fasfood.iamservice.domain.repository.RoleRepository;
import com.fasfood.iamservice.infrastructure.persistence.entity.PermissionEntity;
import com.fasfood.iamservice.infrastructure.persistence.entity.RoleEntity;
import com.fasfood.iamservice.infrastructure.persistence.repository.PermissionEntityRepository;
import com.fasfood.iamservice.infrastructure.persistence.repository.RoleEntityRepository;
import com.fasfood.iamservice.infrastructure.support.exception.BadRequestError;
import com.fasfood.iamservice.infrastructure.support.exception.NotFoundError;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleCommandServiceImpl implements RoleCommandService {

    private final IamCommandMapper iamCommandMapper;
    private final RoleDTOMapper dtoMapper;
    private final RoleRepository roleRepository;
    private final RoleEntityRepository roleEntityRepository;
    private final PermissionEntityRepository permissionEntityRepository;

    @Override
    @Transactional
    public RoleDTO create(CreateOrUpdateRoleRequest request) {
        this.checkExistedRoles(Set.of(request.getName()));
        this.checkNotFoundPermissions(request.getPermissions().stream()
                .map(AssignOrUnassignRolePermissionRequest::getPermissionId).collect(Collectors.toSet()));
        CreateOrUpdateRoleCmd cmd = this.iamCommandMapper.from(request);
        return this.dtoMapper.domainToDTO(this.roleRepository.saveAll(List.of(new Role(cmd)))).getFirst();
    }

    @Override
    @Transactional
    public RoleDTO update(UUID roleId, CreateOrUpdateRoleRequest request) {
        Role found = this.roleRepository.getById(roleId);
        if(!found.getName().equals(request.getName())) this.checkExistedRoles(Set.of(request.getName()));
        this.checkNotFoundPermissions(request.getPermissions().stream()
                .map(AssignOrUnassignRolePermissionRequest::getPermissionId).collect(Collectors.toSet()));
        CreateOrUpdateRoleCmd cmd = this.iamCommandMapper.from(request);
        found.updateRole(cmd);
        return this.dtoMapper.domainToDTO(this.roleRepository.saveAll(List.of(found))).getFirst();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Role found = this.roleRepository.getById(id);
        found.delete();
        this.roleRepository.saveAll(List.of(found));
    }

    private void checkExistedRoles(Set<String> names) {
        List<RoleEntity> found = this.roleEntityRepository.findAllExistedByNames(names);
        if (!CollectionUtils.isEmpty(found)) {
            throw new ResponseException(BadRequestError.ROLE_EXISTED,
                    String.join(",", found.stream().map(RoleEntity::getName).toList()));
        }
    }

    private void checkNotFoundPermissions(Set<UUID> permissionIds) {
        List<PermissionEntity> found = this.permissionEntityRepository.findExistedEntitiesByIds(permissionIds);
        permissionIds.removeAll(found.stream().map(PermissionEntity::getId).collect(Collectors.toSet()));
        if (!CollectionUtils.isEmpty(permissionIds)) {
            throw new ResponseException(NotFoundError.PERMISSION_NOTFOUND,
                    String.join(",", permissionIds.stream().map(UUID::toString).toList()));
        }
    }
}
