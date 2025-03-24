package com.fasfood.storageservice.presentation.rest;

import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.storageservice.application.dto.request.FileActionPagingRequest;
import com.fasfood.storageservice.application.dto.response.FileActionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Files resource")
@RequestMapping("/api/v1/file-actions")
@Validated
public interface FileActionController {
    @GetMapping("")
    @Operation(summary = "Get file actions")
    PagingResponse<FileActionResponse> getFileActions(@ParameterObject FileActionPagingRequest request);
}
