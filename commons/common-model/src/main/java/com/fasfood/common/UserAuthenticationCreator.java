package com.fasfood.common;

import com.fasfood.common.enums.TokenType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@Builder
public class UserAuthenticationCreator {
    private Object principal;
    private Object credentials;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean isRoot;
    private boolean isClient;
    private String email;
    private String fullName;
    private String token;
    private TokenType tokenType;
    private UUID userId;
}
