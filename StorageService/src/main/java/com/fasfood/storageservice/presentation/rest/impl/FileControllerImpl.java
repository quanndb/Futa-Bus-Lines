package com.fasfood.storageservice.presentation.rest.impl;

import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.storageservice.application.dto.request.FilePagingRequest;
import com.fasfood.common.dto.response.FileResponse;
import com.fasfood.storageservice.application.service.cmd.FileCommandService;
import com.fasfood.storageservice.application.service.query.FileQueryService;
import com.fasfood.storageservice.presentation.rest.FileController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FileControllerImpl implements FileController {

    private final FileCommandService fileCommandService;
    private final FileQueryService fileQueryService;

    @Override
    public Response<List<FileResponse>> upload(List<MultipartFile> files, Boolean sharing) throws IOException {
        return Response.of(this.fileCommandService.upload(files, sharing));
    }

    @Override
    public ResponseEntity<byte[]> download(UUID fileId, Integer width, Integer height, Double ratio) throws IOException {
        return this.fileQueryService.downloadFile(fileId, width, height, ratio);
    }

    @Override
    public Response<FileResponse> getFileInfo(UUID fileId) {
        return Response.of(this.fileQueryService.getFileDetails(fileId));
    }

    @Override
    public Response<Void> deleteFile(UUID fileId) {
        this.fileCommandService.delete(fileId);
        return Response.ok();
    }

    @Override
    public PagingResponse<FileResponse> getFiles(FilePagingRequest request) {
        return PagingResponse.of(this.fileQueryService.getFiles(request));
    }
}
