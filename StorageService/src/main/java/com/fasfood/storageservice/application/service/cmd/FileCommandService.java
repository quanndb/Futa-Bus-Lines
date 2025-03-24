package com.fasfood.storageservice.application.service.cmd;

import com.fasfood.common.dto.response.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FileCommandService {
    List<FileResponse> upload(List<MultipartFile> files, Boolean sharing) throws IOException;

    void delete(UUID id);
}
