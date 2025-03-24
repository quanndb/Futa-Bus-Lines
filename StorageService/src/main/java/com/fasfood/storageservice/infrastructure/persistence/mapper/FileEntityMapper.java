package com.fasfood.storageservice.infrastructure.persistence.mapper;

import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.storageservice.domain.File;
import com.fasfood.storageservice.infrastructure.persistence.entity.FileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileEntityMapper extends EntityMapper<File, FileEntity> {
}
