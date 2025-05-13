package com.fasfood.common;

import com.fasfood.common.enums.TokenType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = false)
public class UserAuthentication extends UsernamePasswordAuthenticationToken {
    @Getter
    private final UUID userId;
    private final boolean isRoot;
    private final boolean isClient;
    @Getter
    private final String token;
    @Getter
    private final TokenType tokenType;
    @Getter
    private final String email;
    @Getter
    private final String role;
    @Getter
    private final String fullName;
    @Getter
    private final Set<String> grantedPermissions;

    public UserAuthentication(UserAuthenticationCreator creator) {
        super(creator.getPrincipal(), creator.getCredentials(), creator.getAuthorities());
        this.isRoot = creator.isRoot();
        this.isClient = creator.isClient();
        this.token = creator.getToken();
        this.email = creator.getEmail();
        this.fullName = creator.getFullName();
        this.userId = creator.getUserId();
        this.tokenType = creator.getTokenType();
        this.grantedPermissions = CollectionUtils.isEmpty(creator.getAuthorities())
                ? Collections.emptySet()
                : creator.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        this.role = creator.getRole();
    }

    public boolean isRoot() {
        return isRoot;
    }

    public boolean isClient() {
        return isClient;
    }
}
