package com.fasfood.iamservice.application.service.cmd.impl;

import com.fasfood.client.client.storage.StorageClient;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.iamservice.application.dto.mapper.AccountDTOMapper;
import com.fasfood.iamservice.application.dto.request.ChangePasswordRequest;
import com.fasfood.iamservice.application.dto.request.CreateAccountRequest;
import com.fasfood.iamservice.application.dto.request.RegisterRequest;
import com.fasfood.iamservice.application.dto.request.SetPasswordRequest;
import com.fasfood.iamservice.application.dto.request.UpdateAccountRequest;
import com.fasfood.iamservice.application.dto.request.UpdateProfileRequest;
import com.fasfood.iamservice.application.dto.response.CreateOrUpdateAccountResponse;
import com.fasfood.iamservice.application.dto.response.UpdateAccountResponse;
import com.fasfood.iamservice.application.mapper.IamCommandMapper;
import com.fasfood.iamservice.application.service.cmd.AccountCommandService;
import com.fasfood.iamservice.domain.Account;
import com.fasfood.iamservice.domain.cmd.CreateOrUpdateAccountCmd;
import com.fasfood.iamservice.domain.repository.AccountRepository;
import com.fasfood.iamservice.infrastructure.persistence.entity.RoleEntity;
import com.fasfood.iamservice.infrastructure.persistence.repository.AccountEntityRepository;
import com.fasfood.iamservice.infrastructure.persistence.repository.RoleEntityRepository;
import com.fasfood.iamservice.infrastructure.support.exception.BadRequestError;
import com.fasfood.iamservice.infrastructure.support.exception.NotFoundError;
import com.fasfood.iamservice.infrastructure.support.util.UserAuthoritiesCache;
import com.fasfood.util.FileStorageUtil;
import com.fasfood.web.support.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountCommandServiceImpl implements AccountCommandService {

    private final IamCommandMapper iamCommandMapper;
    private final AccountRepository accountRepository;
    private final AccountEntityRepository accountEntityRepository;
    private final RoleEntityRepository roleEntityRepository;
    private final UserAuthoritiesCache userAuthoritiesCache;
    private final AccountDTOMapper accountDTOMapper;
    private final PasswordEncoder passwordEncoder;
    private final StorageClient storageClient;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        this.checkExistedEmail(request.getEmail());
        CreateOrUpdateAccountCmd cmd = this.iamCommandMapper.from(request);
        cmd.setPassword(this.passwordEncoder.encode(cmd.getPassword()));
        this.accountRepository.saveAll(List.of(new Account(cmd)));
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        if (request.getOldPassword().equals(request.getNewPassword())) {
            throw new ResponseException(BadRequestError.PASSWORD_MUST_BE_DIFFERENT);
        }
        Account found = this.accountRepository.getById(SecurityUtils.getUserId());
        if (!this.passwordEncoder.matches(request.getOldPassword(), found.getPassword())) {
            throw new ResponseException(BadRequestError.WRONG_PASSWORD);
        }
        found.changePassword(this.passwordEncoder.encode(request.getNewPassword()));
        this.accountRepository.saveAll(List.of(found));
    }

    @Override
    public void forgotPassword() {
        // TODO: send action token
    }

    @Override
    @Transactional
    public void setPassword(SetPasswordRequest request) {
        Account found = this.accountRepository.getById(SecurityUtils.getUserId());
        found.changePassword(this.passwordEncoder.encode(request.getNewPassword()));
        this.accountRepository.saveAll(List.of(found));
    }

    @Override
    @Transactional
    public UpdateAccountResponse updateProfile(UpdateProfileRequest request) {
        Account found = this.accountRepository.getById(SecurityUtils.getUserId());
        CreateOrUpdateAccountCmd cmd = this.iamCommandMapper.from(request);
        found.updateAccount(cmd);
        this.accountRepository.saveAll(List.of(found));
        return this.accountDTOMapper.from(found);
    }

    @Override
    public UpdateAccountResponse updateAvatar(MultipartFile image) throws IOException {
        if (!FileStorageUtil.getFileType(image).startsWith("image/")) {
            throw new ResponseException(BadRequestError.INVALID_AVATAR);
        }
        ;
        Account found = this.accountRepository.getById(SecurityUtils.getUserId());
        var files = this.storageClient.upload(List.of(image), true);
        found.updateAvatar(files.getData().getFirst().getPath());
        this.accountRepository.saveAll(List.of(found));
        return this.accountDTOMapper.from(found);
    }

    @Override
    @Transactional
    public CreateOrUpdateAccountResponse create(CreateAccountRequest request) {
        this.checkExistedEmail(request.getEmail());
        this.checkNotFoundRoles(new HashSet<>(request.getRoleIds()));
        CreateOrUpdateAccountCmd cmd = this.iamCommandMapper.from(request);
        cmd.setPassword(this.passwordEncoder.encode(cmd.getPassword()));
        Account newAccount = new Account(cmd);
        this.accountRepository.saveAll(List.of(newAccount));
        return this.accountDTOMapper.domainToDTO(newAccount);
    }

    @Override
    @Transactional
    public CreateOrUpdateAccountResponse update(UUID id, UpdateAccountRequest request) {
        this.checkNotFoundRoles(new HashSet<>(request.getRoleIds()));
        Account found = this.accountRepository.getById(id);
        CreateOrUpdateAccountCmd cmd = this.iamCommandMapper.from(request);
        found.updateAccount(cmd);
        this.userAuthoritiesCache.remove(id);
        if (Objects.nonNull(request.getPassword())) {
            found.changePassword(this.passwordEncoder.encode(cmd.getPassword()));
        }
        this.accountRepository.saveAll(List.of(found));
        return this.accountDTOMapper.domainToDTO(found);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Account found = this.accountRepository.getById(SecurityUtils.getUserId());
        found.delete();
        this.accountRepository.saveAll(List.of(found));
    }

    private void checkExistedEmail(String email) {
        this.accountEntityRepository.findByEmail(email)
                .ifPresent((item) -> {
                    throw new ResponseException(BadRequestError.EMAIL_EXISTED, item.getEmail());
                });
    }

    private void checkNotFoundRoles(Set<UUID> roleIds) {
        List<RoleEntity> found = this.roleEntityRepository.findAllById(roleIds);
        roleIds.removeAll(found.stream().map(RoleEntity::getId).collect(Collectors.toSet()));
        if (!CollectionUtils.isEmpty(roleIds)) {
            throw new ResponseException(NotFoundError.ROLE_NOTFOUND,
                    String.join(",", roleIds.stream().map(UUID::toString).collect(Collectors.toSet())));
        }
    }
}
