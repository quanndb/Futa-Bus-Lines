package com.fasfood.storageservice.presentation.rest;

import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.storageservice.application.dto.request.FilePagingRequest;
import com.fasfood.common.dto.response.FileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(name = "Files resource")
@RequestMapping("/api/v1/files")
@Validated
public interface FileController {

    @Operation(summary = "Upload multiple files")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Response<List<FileResponse>> upload(
            @RequestPart("files") List<MultipartFile> files,
            @RequestParam("sharing") Boolean sharing
    ) throws IOException;

    @GetMapping("/{fileId}")
    @Operation(summary = "Download file")
    ResponseEntity<byte[]> download(@PathVariable UUID fileId,
                                    @RequestParam(required = false) Integer width,
                                    @RequestParam(required = false) Integer height,
                                    @RequestParam(required = false) Double ratio) throws IOException;

    @GetMapping("/{fileId}/info")
    @Operation(summary = "Get file information")
    @PreAuthorize("hasPermission(null, 'file.view')")
    Response<FileResponse> getFileInfo(@PathVariable UUID fileId);

    @DeleteMapping("/{fileId}")
    @Operation(summary = "Delete file")
    Response<Void> deleteFile(@PathVariable UUID fileId);

    @GetMapping("")
    @Operation(summary = "Get files")
    PagingResponse<FileResponse> getFiles(@ParameterObject FilePagingRequest request);
}
