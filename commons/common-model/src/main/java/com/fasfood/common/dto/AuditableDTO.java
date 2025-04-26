package com.fasfood.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class AuditableDTO implements Serializable {
    protected String createdBy;
    protected Instant createdAt;
    protected String lastModifiedBy;
    protected Instant lastModifiedAt;
}
