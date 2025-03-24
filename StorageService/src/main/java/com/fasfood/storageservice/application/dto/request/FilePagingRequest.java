package com.fasfood.storageservice.application.dto.request;

import com.fasfood.common.dto.request.PagingRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class FilePagingRequest extends PagingRequest {
    private Instant fromAt;
    private Instant toAt;
    private List<Boolean> sharing;
    private List<String> type;
    private List<String> extension;
    private UUID ownerId;
}
