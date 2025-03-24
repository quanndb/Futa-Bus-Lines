package com.fasfood.iamservice.domain.repository;

import com.fasfood.iamservice.domain.Account;
import com.fasfood.web.support.DomainRepository;

import java.util.UUID;

public interface AccountRepository extends DomainRepository<Account, UUID> {
}
