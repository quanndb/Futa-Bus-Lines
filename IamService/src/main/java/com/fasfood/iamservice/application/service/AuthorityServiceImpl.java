package com.fasfood.iamservice.application.service;

import com.fasfood.common.UserAuthority;
import com.fasfood.iamservice.application.dto.response.AccountRoleResponse;
import com.fasfood.iamservice.application.dto.response.CreateOrUpdateAccountResponse;
import com.fasfood.iamservice.application.service.query.AccountQueryService;
import com.fasfood.iamservice.infrastructure.support.util.UserAuthoritiesCache;
import com.fasfood.web.security.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@Component
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {

    private final AccountQueryService accountQueryService;
    private final UserAuthoritiesCache userAuthoritiesCache;

    @Override
    public UserAuthority getUserAuthority(UUID userId) {
        if (this.userAuthoritiesCache.hasKey(userId)){
            return this.userAuthoritiesCache.get(userId);
        }
        CreateOrUpdateAccountResponse response = this.accountQueryService.getById(userId);
        boolean isRoot = response.getRoles().stream().anyMatch(AccountRoleResponse::getIsRoot);
        UserAuthority res = UserAuthority.builder()
                .userId(userId)
                .grantedPermissions(response.getGrantedPermissions())
                .role(response.getRoles().stream().map(AccountRoleResponse::getName).collect(Collectors.joining(", ")))
                .isRoot(isRoot)
                .build();
        this.userAuthoritiesCache.put(res.getUserId(), res);
        return res;
    }

    @Override
    public UserAuthority getUserAuthority(String username) {
        return null;
    }

    @Override
    public UserAuthority getClientAuthority(UUID clientId) {
        return null;
    }
}
