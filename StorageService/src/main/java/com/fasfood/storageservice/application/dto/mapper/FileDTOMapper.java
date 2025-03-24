package com.fasfood.storageservice.application.dto.mapper;

import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.common.dto.response.FileResponse;
import com.fasfood.storageservice.domain.File;
import com.fasfood.storageservice.infrastructure.persistence.entity.FileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileDTOMapper extends DTOMapper<FileResponse, File, FileEntity> {
}
