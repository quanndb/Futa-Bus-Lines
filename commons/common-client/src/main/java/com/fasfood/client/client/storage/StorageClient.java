package com.fasfood.client.client.storage;

import com.fasfood.client.config.FeignClientConfiguration;
import com.fasfood.common.dto.response.FileResponse;
import com.fasfood.common.dto.response.Response;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@FeignClient(
        name = "storage",
        contextId = "storage",
        configuration = {FeignClientConfiguration.class},
        fallbackFactory = StorageClientFallback.class
)
public interface StorageClient {
    @PostMapping(value = "/api/v1/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Headers("Content-Type: multipart/form-data")
    Response<List<FileResponse>> upload(
            @RequestPart("files") List<MultipartFile> files,
            @RequestParam("sharing") Boolean sharing
    ) throws IOException;

    @GetMapping(value = "/api/v1/files/{fileId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<byte[]> download(@PathVariable UUID fileId,
                                    @RequestParam(required = false) Integer width,
                                    @RequestParam(required = false) Integer height,
                                    @RequestParam(required = false) Double ratio) throws IOException;

    @GetMapping(value = "/api/v1/files/{fileId}/info", consumes = MediaType.APPLICATION_JSON_VALUE)
    Response<FileResponse> getFileInfo(@PathVariable UUID fileId);

    @DeleteMapping(value = "/api/v1/files/{fileId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    Response<Void> deleteFile(@PathVariable UUID fileId);
}
