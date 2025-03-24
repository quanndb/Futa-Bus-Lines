package com.fasfood.iamservice.domain.cmd;

import com.fasfood.common.enums.Scope;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AssignOrUnassignPermissionCmd {
    private UUID permissionId;
    private Scope scope;
}
