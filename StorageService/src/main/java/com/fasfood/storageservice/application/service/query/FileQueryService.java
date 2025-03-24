package com.fasfood.storageservice.application.service.query;

import com.fasfood.common.dto.PageDTO;
import com.fasfood.storageservice.application.dto.request.FileActionPagingRequest;
import com.fasfood.storageservice.application.dto.request.FilePagingRequest;
import com.fasfood.storageservice.application.dto.response.FileActionResponse;
import com.fasfood.common.dto.response.FileResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.UUID;

public interface FileQueryService {
    ResponseEntity<byte[]> downloadFile(UUID fileId, Integer width, Integer height, Double ratio) throws IOException;

    FileResponse getFileDetails(UUID fileId);

    PageDTO<FileResponse> getFiles(FilePagingRequest request);

    PageDTO<FileActionResponse> getFileActions(FileActionPagingRequest request);
}
