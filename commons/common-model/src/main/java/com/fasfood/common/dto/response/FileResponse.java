package com.fasfood.common.dto.response;

import com.fasfood.common.dto.AuditableDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FileResponse extends AuditableDTO {
    private UUID id;
    private UUID ownerId;
    private String name;
    private String path;
    private String type;
    private String extension;
    private boolean sharing;
    private long size;
}
