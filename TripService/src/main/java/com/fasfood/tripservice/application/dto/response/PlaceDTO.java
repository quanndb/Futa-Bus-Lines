package com.fasfood.tripservice.application.dto.response;

import com.fasfood.common.dto.AuditableDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDTO extends AuditableDTO {
    private UUID id;
    private String name;
    private String address;
    private String code;
}
