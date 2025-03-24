package com.fasfood.iamservice.application.service.query.impl;

import com.fasfood.common.dto.PageDTO;
import com.fasfood.iamservice.application.dto.mapper.PermissionDTOMapper;
import com.fasfood.iamservice.application.dto.request.PermissionPagingRequest;
import com.fasfood.iamservice.application.dto.response.PermissionDTO;
import com.fasfood.iamservice.application.mapper.IamQueryMapper;
import com.fasfood.iamservice.application.service.query.PermissionQueryService;
import com.fasfood.iamservice.domain.query.PermissionPagingQuery;
import com.fasfood.iamservice.infrastructure.persistence.repository.PermissionEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionQueryServiceImpl implements PermissionQueryService {

    private final PermissionEntityRepository permissionEntityRepository;
    private final PermissionDTOMapper permissionDTOMapper;
    private final IamQueryMapper iamQueryMapper;

    @Override
    public PageDTO<PermissionDTO> getPermissions(PermissionPagingRequest request) {
        PermissionPagingQuery query = this.iamQueryMapper.from(request);
        long count = this.permissionEntityRepository.count(query);
        if (count == 0) return PageDTO.empty();
        List<PermissionDTO> data = this.permissionDTOMapper.entityToDTO(this.permissionEntityRepository.search(query));
        return PageDTO.of(data, request.getPageIndex(), request.getPageSize(), count);
    }
}
