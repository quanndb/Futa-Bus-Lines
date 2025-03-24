package com.fasfood.client.client.storage;

import com.fasfood.common.dto.response.FileResponse;
import com.fasfood.common.dto.response.Response;
import com.fasfood.common.error.ServiceUnavailableError;
import com.fasfood.common.exception.ForwardInnerAlertException;
import com.fasfood.common.exception.ResponseException;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@NoArgsConstructor
public class StorageClientFallback implements FallbackFactory<StorageClient> {
    @Override
    public StorageClient create(Throwable cause) {
        return new StorageClientFallback.FallbackWithFactory(cause);
    }

    static class FallbackWithFactory implements StorageClient {
        private static final Logger log = LoggerFactory.getLogger(StorageClientFallback.FallbackWithFactory.class);
        private final Throwable cause;

        FallbackWithFactory(Throwable cause) {
            this.cause = cause;
        }

        @Override
        public Response<List<FileResponse>> upload(List<MultipartFile> files, Boolean sharing) throws IOException {
            log.error("Upload files", this.cause);
            return this.cause instanceof ForwardInnerAlertException ? Response.fail((RuntimeException) this.cause)
                    : Response.fail(new ResponseException(ServiceUnavailableError.SERVICE_UNAVAILABLE_ERROR));
        }

        @Override
        public ResponseEntity<byte[]> download(UUID fileId, Integer width, Integer height, Double ratio) throws IOException {
            log.error("Download file", this.cause);
            return this.cause instanceof ForwardInnerAlertException ? ResponseEntity.badRequest().body(new byte[0])
                    : ResponseEntity.internalServerError().body(new byte[0]);
        }

        @Override
        public Response<FileResponse> getFileInfo(UUID fileId) {
            log.error("Get file info", this.cause);
            return this.cause instanceof ForwardInnerAlertException ? Response.fail((RuntimeException) this.cause)
                    : Response.fail(new ResponseException(ServiceUnavailableError.SERVICE_UNAVAILABLE_ERROR));
        }

        @Override
        public Response<Void> deleteFile(UUID fileId) {
            log.error("Delete file info", this.cause);
            return this.cause instanceof ForwardInnerAlertException ? Response.fail((RuntimeException) this.cause)
                    : Response.fail(new ResponseException(ServiceUnavailableError.SERVICE_UNAVAILABLE_ERROR));
        }
    }
}
