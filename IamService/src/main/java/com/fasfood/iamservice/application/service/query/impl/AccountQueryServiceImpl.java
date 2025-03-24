package com.fasfood.iamservice.application.service.query.impl;

import com.fasfood.common.dto.PageDTO;
import com.fasfood.iamservice.application.dto.mapper.AccountDTOMapper;
import com.fasfood.iamservice.application.dto.request.AccountPagingRequest;
import com.fasfood.iamservice.application.dto.response.AccountRoleResponse;
import com.fasfood.iamservice.application.dto.response.CreateOrUpdateAccountResponse;
import com.fasfood.iamservice.application.dto.response.UpdateAccountResponse;
import com.fasfood.iamservice.application.mapper.IamQueryMapper;
import com.fasfood.iamservice.application.service.query.AccountQueryService;
import com.fasfood.iamservice.domain.Account;
import com.fasfood.iamservice.domain.query.AccountPagingQuery;
import com.fasfood.iamservice.domain.repository.AccountRepository;
import com.fasfood.iamservice.infrastructure.persistence.entity.AccountEntity;
import com.fasfood.iamservice.infrastructure.persistence.entity.PermissionEntity;
import com.fasfood.iamservice.infrastructure.persistence.entity.RoleEntity;
import com.fasfood.iamservice.infrastructure.persistence.entity.RolePermissionEntity;
import com.fasfood.iamservice.infrastructure.persistence.repository.AccountEntityRepository;
import com.fasfood.iamservice.infrastructure.persistence.repository.PermissionEntityRepository;
import com.fasfood.iamservice.infrastructure.persistence.repository.RoleEntityRepository;
import com.fasfood.iamservice.infrastructure.persistence.repository.RolePermissionEntityRepository;
import com.fasfood.web.support.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountQueryServiceImpl implements AccountQueryService {

    private final AccountRepository accountRepository;
    private final AccountEntityRepository accountEntityRepository;
    private final RoleEntityRepository roleEntityRepository;
    private final RolePermissionEntityRepository rolePermissionEntityRepository;
    private final PermissionEntityRepository permissionEntityRepository;
    private final AccountDTOMapper accountDTOMapper;
    private final IamQueryMapper iamQueryMapper;

    @Override
    public PageDTO<UpdateAccountResponse> getAccounts(AccountPagingRequest request) {
        AccountPagingQuery query = this.iamQueryMapper.from(request);
        long count = this.accountEntityRepository.count(query);
        if (count == 0) return PageDTO.empty();
        List<AccountEntity> entities = this.accountEntityRepository.search(query);
        List<UpdateAccountResponse> dto = this.accountDTOMapper.from(entities);
        return PageDTO.of(dto, request.getPageIndex(), request.getPageSize(), count);
    }

    @Override
    public CreateOrUpdateAccountResponse getById(UUID id) {
        Account found = this.accountRepository.getById(id);
        CreateOrUpdateAccountResponse response = this.accountDTOMapper.domainToDTO(found);
        this.enrichRole(response);
        return response;
    }

    @Override
    public UpdateAccountResponse getMyProfile() {
        Account found = this.accountRepository.getById(SecurityUtils.getUserId());
        return this.accountDTOMapper.from(found);
    }

    private void enrichRole(CreateOrUpdateAccountResponse account) {
        //enrich roles
        List<UUID> roleIds = account.getRoles().stream().map(AccountRoleResponse::getRoleId).toList();
        Map<UUID, RoleEntity> roleMap = this.roleEntityRepository
                .findAllById(roleIds)
                .stream().collect(Collectors.toMap(RoleEntity::getId, Function.identity()));
        account.getRoles().forEach(role -> {
            if (roleMap.containsKey(role.getRoleId())) {
                RoleEntity roleEntity = roleMap.get(role.getRoleId());
                role.setName(roleEntity.getName());
                role.setIsRoot(roleEntity.getIsRoot());
            }
        });
        //enrich authorities
        List<RolePermissionEntity> rolePermissionEntities = this.rolePermissionEntityRepository.findAllByRoleIds(roleIds);
        Set<UUID> permissionIds = rolePermissionEntities.stream().map(RolePermissionEntity::getPermissionId).collect(Collectors.toSet());
        Map<UUID, PermissionEntity> permissionEntitiesMap = this.permissionEntityRepository.findAllById(permissionIds)
                .stream().collect(Collectors.toMap(PermissionEntity::getId, Function.identity()));
        List<String> grantedPermissions = new ArrayList<>();
        rolePermissionEntities.forEach(rolePermission -> {
            if (permissionEntitiesMap.containsKey(rolePermission.getPermissionId())) {
                grantedPermissions.add(String.join(".", permissionEntitiesMap
                        .get(rolePermission.getPermissionId()).getName(), rolePermission.getScope().toString().toLowerCase()));
            }
        });
        account.setGrantedPermissions(grantedPermissions);
    }
}
