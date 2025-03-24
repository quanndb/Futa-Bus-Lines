package com.fasfood.iamservice.infrastructure.domainrepository;

import com.fasfood.common.exception.ResponseException;
import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.iamservice.domain.Account;
import com.fasfood.iamservice.domain.AccountRole;
import com.fasfood.iamservice.domain.repository.AccountRepository;
import com.fasfood.iamservice.infrastructure.persistence.entity.AccountEntity;
import com.fasfood.iamservice.infrastructure.persistence.entity.AccountRoleEntity;
import com.fasfood.iamservice.infrastructure.persistence.mapper.AccountRoleEntityMapper;
import com.fasfood.iamservice.infrastructure.persistence.repository.AccountRoleEntityRepository;
import com.fasfood.iamservice.infrastructure.support.exception.NotFoundError;
import com.fasfood.web.support.AbstractDomainRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class AccountRepositoryImpl extends AbstractDomainRepository<Account, AccountEntity, UUID>
        implements AccountRepository {

    private final AccountRoleEntityRepository accountRoleEntityRepository;
    private final AccountRoleEntityMapper accountRoleEntityMapper;

    protected AccountRepositoryImpl(JpaRepository<AccountEntity, UUID> jpaRepository,
                                    EntityMapper<Account, AccountEntity> mapper,
                                    AccountRoleEntityRepository accountRoleEntityRepository,
                                    AccountRoleEntityMapper accountRoleEntityMapper) {
        super(jpaRepository, mapper);
        this.accountRoleEntityRepository = accountRoleEntityRepository;
        this.accountRoleEntityMapper = accountRoleEntityMapper;
    }

    @Override
    @Transactional
    public List<Account> saveAll(List<Account> domains) {
        Objects.requireNonNull(domains);
        List<AccountRole> accountRoles = new ArrayList<>();
        domains.forEach(domain -> {
            accountRoles.addAll(domain.getRoles());
        });
        this.accountRoleEntityRepository.saveAll(this.accountRoleEntityMapper.toEntity(accountRoles));
        return super.saveAll(domains);
    }

    @Override
    public Account getById(UUID id) {
        return this.findById(id).orElseThrow(() -> new ResponseException(NotFoundError.ACCOUNT_NOTFOUND));
    }

    @Override
    protected List<Account> enrichList(List<Account> accounts) {
        Objects.requireNonNull(accounts);

        List<AccountRoleEntity> accountRoleEntities = this.accountRoleEntityRepository
                .findAllByAccountId(accounts.stream().map(Account::getId).collect(Collectors.toList()));

        Map<UUID, List<AccountRole>> accountRoleMap = this.accountRoleEntityMapper.toDomain(accountRoleEntities)
                .stream().collect(Collectors.groupingBy(AccountRole::getAccountId));

        accounts.forEach(account -> {
            if (accountRoleMap.containsKey(account.getId())) {
                account.enrichAccountRoles(new HashSet<>(accountRoleMap.get(account.getId())));
            }
            else {
                account.enrichAccountRoles(new HashSet<>());
            }
        });

        return accounts;
    }
}
