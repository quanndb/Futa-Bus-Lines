package com.fasfood.storageservice.infrastructure.persistence.mapper;

import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.storageservice.domain.FileAction;
import com.fasfood.storageservice.infrastructure.persistence.entity.FileActionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileActionEntityMapper extends EntityMapper<FileAction, FileActionEntity> {
}
