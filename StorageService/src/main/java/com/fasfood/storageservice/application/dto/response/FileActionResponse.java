package com.fasfood.storageservice.application.dto.response;

import com.fasfood.common.dto.AuditableDTO;
import com.fasfood.storageservice.infrastructure.support.enums.FileActionEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
public class FileActionResponse extends AuditableDTO {
    private UUID id;
    private FileActionEnum action;
    private UUID fileId;
    private UUID senderId;
}
