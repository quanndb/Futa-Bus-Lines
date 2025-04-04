package com.fasfood.notificationservice.application.dto.response;

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
public class EmailTemplateDTO extends AuditableDTO {
    private UUID id;
    private String name;
    private String description;
    private String code;
    private String body;
}
