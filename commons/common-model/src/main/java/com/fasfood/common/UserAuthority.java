package com.fasfood.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAuthority {
    private UUID userId;
    private Boolean isRoot;
    private String role;
    private List<String> grantedPermissions;
}
