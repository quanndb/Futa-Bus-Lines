package com.fasfood.web.security;

import com.fasfood.common.UserAuthority;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface AuthorityService {
    UserAuthority getUserAuthority(UUID userId);

    UserAuthority getUserAuthority(String username);

    UserAuthority getClientAuthority(UUID clientId);

    default List<String> getGrantedPermissions(List<String> rolePermission) {
        Set<String> grantedPermissions = new HashSet<>();
        if (!CollectionUtils.isEmpty(rolePermission)) {
            grantedPermissions.addAll(rolePermission);
        }
        return new ArrayList<>(grantedPermissions);
    }
}
