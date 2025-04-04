package com.fasfood.web.security.impl;

import com.fasfood.client.client.iam.IamClient;
import com.fasfood.common.UserAuthority;
import com.fasfood.web.security.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RemoteAuthorityServiceImpl implements AuthorityService {

    private final IamClient iamClient;

    @Override
    public UserAuthority getUserAuthority(UUID userId) {
        return this.iamClient.getUserAuthority(userId).getData();
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
