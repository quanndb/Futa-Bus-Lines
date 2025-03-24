package com.fasfood.iamservice.domain.cmd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreateOrUpdateRoleCmd {
    private String name;
    private String description;
    private Boolean isRoot;
    private Set<AssignOrUnassignPermissionCmd> permissions;
}
