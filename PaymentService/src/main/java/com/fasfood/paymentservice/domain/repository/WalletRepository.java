package com.fasfood.paymentservice.domain.repository;

import com.fasfood.paymentservice.domain.Wallet;
import com.fasfood.web.support.DomainRepository;

import java.util.UUID;

public interface WalletRepository extends DomainRepository<Wallet, UUID> {
    Wallet getByUserId(UUID userId);

    Wallet getWalletByUserIdAndSelectedCommand(UUID userId, UUID commandId);
}
