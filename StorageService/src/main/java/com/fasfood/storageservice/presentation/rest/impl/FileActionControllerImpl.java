package com.fasfood.storageservice.presentation.rest.impl;

import com.fasfood.common.dto.response.PagingResponse;
import com.fasfood.storageservice.application.dto.request.FileActionPagingRequest;
import com.fasfood.storageservice.application.dto.response.FileActionResponse;
import com.fasfood.storageservice.application.service.query.FileQueryService;
import com.fasfood.storageservice.presentation.rest.FileActionController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FileActionControllerImpl implements FileActionController {

    private final FileQueryService fileQueryService;

    @Override
    public PagingResponse<FileActionResponse> getFileActions(FileActionPagingRequest request) {
        return PagingResponse.of(this.fileQueryService.getFileActions(request));
    }
}
