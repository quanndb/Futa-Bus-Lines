package com.fasfood.storageservice.application.dto.mapper;

import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.storageservice.application.dto.response.FileActionResponse;
import com.fasfood.storageservice.domain.FileAction;
import com.fasfood.storageservice.infrastructure.persistence.entity.FileActionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileActionDTOMapper extends DTOMapper<FileActionResponse, FileAction, FileActionEntity> {
}
