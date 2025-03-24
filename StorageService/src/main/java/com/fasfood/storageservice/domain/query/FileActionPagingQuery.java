package com.fasfood.storageservice.domain.query;

import com.fasfood.common.query.PagingQuery;
import com.fasfood.storageservice.infrastructure.support.enums.FileActionEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
public class FileActionPagingQuery extends PagingQuery {
    private Instant fromAt;
    private Instant toAt;
    private List<FileActionEnum> actions;
    private List<UUID> fileId;
    private List<UUID> senderId;
}
