package com.fasfood.storageservice.application.dto.request;

import com.fasfood.common.dto.request.PagingRequest;
import com.fasfood.storageservice.infrastructure.support.enums.FileActionEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class FileActionPagingRequest extends PagingRequest {
    private Instant fromAt;
    private Instant toAt;
    private List<FileActionEnum> actions;
    private List<UUID> fileId;
    private List<UUID> senderId;
}
