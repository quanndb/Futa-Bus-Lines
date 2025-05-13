package com.fasfood.storageservice.application.service.query.impl;

import com.fasfood.common.UserAuthentication;
import com.fasfood.common.dto.PageDTO;
import com.fasfood.common.error.AuthenticationError;
import com.fasfood.common.error.AuthorizationError;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.storageservice.application.dto.mapper.FileActionDTOMapper;
import com.fasfood.storageservice.application.dto.mapper.FileDTOMapper;
import com.fasfood.storageservice.application.dto.request.FileActionPagingRequest;
import com.fasfood.storageservice.application.dto.request.FilePagingRequest;
import com.fasfood.storageservice.application.dto.response.FileActionResponse;
import com.fasfood.common.dto.response.FileResponse;
import com.fasfood.storageservice.application.mapper.StorageQueryMapper;
import com.fasfood.storageservice.application.service.query.FileQueryService;
import com.fasfood.storageservice.domain.File;
import com.fasfood.storageservice.domain.query.FileActionPagingQuery;
import com.fasfood.storageservice.domain.query.FilePagingQuery;
import com.fasfood.storageservice.domain.repository.FileRepository;
import com.fasfood.storageservice.infrastructure.persistence.entity.FileActionEntity;
import com.fasfood.storageservice.infrastructure.persistence.entity.FileEntity;
import com.fasfood.storageservice.infrastructure.persistence.repository.FileActionEntityRepository;
import com.fasfood.storageservice.infrastructure.persistence.repository.FileEntityRepository;
import com.fasfood.storageservice.infrastructure.support.exception.BadRequestError;
import com.fasfood.util.FileStorageUtil;
import com.fasfood.web.security.RegexPermissionEvaluator;
import com.fasfood.web.support.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileQueryServiceImpl implements FileQueryService {

    private final FileRepository fileRepository;
    private final FileEntityRepository fileEntityRepository;
    private final FileActionEntityRepository fileActionEntityRepository;
    private final FileDTOMapper fileDTOMapper;
    private final FileActionDTOMapper fileActionDTOMapper;
    private final RegexPermissionEvaluator permissionEvaluator;
    private final StorageQueryMapper storageQueryMapper;

    @Override
    public ResponseEntity<byte[]> downloadFile(UUID fileId, Integer width, Integer height, Double ratio) throws IOException {
        File found = this.fileRepository.getById(fileId);
        this.checkPermission(found);
        Path filePath = FileStorageUtil.getFilePath(found.getCreatedAt()
                .atZone(ZoneId.systemDefault()).toLocalDate(),
                String.join(".", found.getPath(), found.getExtension()));
        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists()) {
            throw new ResponseException(BadRequestError.FILE_NOT_FOUND);
        }
        Resource returnResource = FileStorageUtil.isImageFile(found.getType())
                ? FileStorageUtil.resizeImage(filePath, width, height, ratio)
                : resource;
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(found.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + returnResource.getFilename() + "\"")
                .body(resource.getContentAsByteArray());
    }

    @Override
    public FileResponse getFileDetails(UUID fileId) {
        File found = this.fileRepository.getById(fileId);
        this.checkPermission(found);
        return this.fileDTOMapper.domainToDTO(this.fileRepository.getById(fileId));
    }

    @Override
    public PageDTO<FileResponse> getFiles(FilePagingRequest request) {
        FilePagingQuery query = this.storageQueryMapper.from(request);
        long count = this.fileEntityRepository.count(query);
        if (count == 0) {
            return PageDTO.empty();
        }
        List<FileEntity> data = this.fileEntityRepository.search(query);
        return PageDTO.of(this.fileDTOMapper.entityToDTO(data), request.getPageIndex(), request.getPageSize(), count);
    }

    @Override
    public PageDTO<FileActionResponse> getFileActions(FileActionPagingRequest request) {
        FileActionPagingQuery query = this.storageQueryMapper.from(request);
        long count = this.fileActionEntityRepository.count(query);
        if (count == 0) {
            return PageDTO.empty();
        }
        List<FileActionEntity> data = this.fileActionEntityRepository.search(query);
        return PageDTO.of(this.fileActionDTOMapper.entityToDTO(data), request.getPageIndex(), request.getPageSize(), count);
    }

    private void checkPermission(File found) {
        if (!found.isSharing()) {
            UserAuthentication userAuthentication = SecurityUtils.getUserAuthentication()
                    .orElseThrow(() -> new ResponseException(AuthenticationError.UNAUTHORISED));
            boolean isAbleToRead;
            if (Objects.equals(found.getOwnerId(), SecurityUtils.getUserId())) {
                isAbleToRead = true;
            } else {
                isAbleToRead = this.permissionEvaluator.hasPermission(userAuthentication, null, "file.read");
            }
            if (!isAbleToRead) {
                throw new ResponseException(AuthorizationError.ACCESS_DENIED);
            }
        }
    }
}
