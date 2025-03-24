package com.fasfood.iamservice.application.service.query.impl;

import com.fasfood.common.dto.PageDTO;
import com.fasfood.iamservice.application.dto.mapper.RoleDTOMapper;
import com.fasfood.iamservice.application.dto.request.RolePagingRequest;
import com.fasfood.iamservice.application.dto.response.RoleDTO;
import com.fasfood.iamservice.application.mapper.IamQueryMapper;
import com.fasfood.iamservice.application.service.query.RoleQueryService;
import com.fasfood.iamservice.domain.Role;
import com.fasfood.iamservice.domain.query.RolePagingQuery;
import com.fasfood.iamservice.domain.repository.RoleRepository;
import com.fasfood.iamservice.infrastructure.persistence.repository.RoleEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleQueryServiceImpl implements RoleQueryService {

    private final RoleEntityRepository roleEntityRepository;
    private final RoleDTOMapper roleDTOMapper;
    private final IamQueryMapper iamQueryMapper;
    private final RoleRepository roleRepository;

    @Override
    public PageDTO<RoleDTO> getRoles(RolePagingRequest request) {
        RolePagingQuery query = this.iamQueryMapper.from(request);
        long count = this.roleEntityRepository.count(query);
        if(count == 0) return PageDTO.empty();
        List<RoleDTO> data = this.roleDTOMapper.entityToDTO(this.roleEntityRepository.search(query));
        return PageDTO.of(data, request.getPageIndex(), request.getPageSize(), count);
    }

    @Override
    public RoleDTO getRoleDetails(UUID id) {
        Role found = this.roleRepository.getById(id);
        return this.roleDTOMapper.domainToDTO(found);
    }
}
