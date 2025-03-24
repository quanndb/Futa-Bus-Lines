package com.fasfood.storageservice.domain.query;

import com.fasfood.common.query.PagingQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
public class FilePagingQuery extends PagingQuery {
    private Instant fromAt;
    private Instant toAt;
    private List<Boolean> sharing;
    private List<String> type;
    private List<String> extension;
    private UUID ownerId;
}
