package com.fasfood.storageservice.domain;

import com.fasfood.common.domain.AuditableDomain;
import com.fasfood.storageservice.infrastructure.support.enums.FileActionEnum;
import com.fasfood.util.IdUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class FileAction extends AuditableDomain {
    private UUID id;
    private FileActionEnum action;
    private UUID fileId;
    private UUID senderId;
    private Boolean deleted;

    public FileAction(UUID fileId, UUID senderId, FileActionEnum action) {
        this.id = IdUtils.nextId();
        this.fileId = fileId;
        this.senderId = senderId;
        this.action = action;
        this.deleted = Boolean.FALSE;
    }
}
