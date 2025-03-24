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

@Primary
@Component
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {

    private final AccountQueryService accountQueryService;
    private final UserAuthoritiesCache userAuthoritiesCache;

    @Override
    public UserAuthority getUserAuthority(UUID userId) {
        if (this.userAuthoritiesCache.hasUserAuthorities(userId)){
            return this.userAuthoritiesCache.getUserAuthorities(userId);
        }
        CreateOrUpdateAccountResponse response = this.accountQueryService.getById(userId);
        boolean isRoot = response.getRoles().stream().anyMatch(AccountRoleResponse::getIsRoot);
        UserAuthority res = UserAuthority.builder()
                .userId(userId)
                .grantedPermissions(response.getGrantedPermissions())
                .isRoot(isRoot)
                .build();
        this.userAuthoritiesCache.putUserAuthorities(res);
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
