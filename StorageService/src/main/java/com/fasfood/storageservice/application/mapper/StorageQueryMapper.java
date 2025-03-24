package com.fasfood.storageservice.application.mapper;

import com.fasfood.storageservice.application.dto.request.FileActionPagingRequest;
import com.fasfood.storageservice.application.dto.request.FilePagingRequest;
import com.fasfood.storageservice.domain.query.FileActionPagingQuery;
import com.fasfood.storageservice.domain.query.FilePagingQuery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StorageQueryMapper {
    FilePagingQuery from(FilePagingRequest request);

    FileActionPagingQuery from(FileActionPagingRequest request);
}
