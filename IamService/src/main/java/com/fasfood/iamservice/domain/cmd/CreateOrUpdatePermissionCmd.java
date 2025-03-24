package com.fasfood.iamservice.domain.cmd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreateOrUpdatePermissionCmd {
    private String name;
    private String description;
    private String code;
}
