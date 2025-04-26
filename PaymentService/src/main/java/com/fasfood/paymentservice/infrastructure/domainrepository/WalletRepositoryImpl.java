package com.fasfood.paymentservice.infrastructure.domainrepository;

import com.fasfood.common.exception.ResponseException;
import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.paymentservice.domain.Wallet;
import com.fasfood.paymentservice.domain.WalletCommand;
import com.fasfood.paymentservice.domain.WalletHistory;
import com.fasfood.paymentservice.domain.repository.WalletRepository;
import com.fasfood.paymentservice.infrastructure.persistence.entity.WalletEntity;
import com.fasfood.paymentservice.infrastructure.persistence.mapper.WalletCommandEntityMapper;
import com.fasfood.paymentservice.infrastructure.persistence.mapper.WalletHistoryEntityMapper;
import com.fasfood.paymentservice.infrastructure.persistence.repository.WalletCommandEntityRepository;
import com.fasfood.paymentservice.infrastructure.persistence.repository.WalletEntityRepository;
import com.fasfood.paymentservice.infrastructure.persistence.repository.WalletHistoryEntityRepository;
import com.fasfood.paymentservice.infrastructure.support.exception.NotFoundError;
import com.fasfood.web.support.AbstractDomainRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class WalletRepositoryImpl extends AbstractDomainRepository<Wallet, WalletEntity, UUID> implements WalletRepository {

    private final WalletEntityRepository walletEntityRepository;
    private final WalletCommandEntityRepository walletCommandEntityRepository;
    private final WalletCommandEntityMapper walletCommandEntityMapper;
    private final WalletHistoryEntityRepository walletHistoryEntityRepository;
    private final WalletHistoryEntityMapper walletHistoryEntityMapper;

    protected WalletRepositoryImpl(JpaRepository<WalletEntity, UUID> jpaRepository,
                                   EntityMapper<Wallet, WalletEntity> mapper,
                                   WalletEntityRepository walletEntityRepository,
                                   WalletCommandEntityRepository walletCommandEntityRepository,
                                   WalletCommandEntityMapper walletCommandEntityMapper,
                                   WalletHistoryEntityRepository walletHistoryEntityRepository,
                                   WalletHistoryEntityMapper walletHistoryEntityMapper) {
        super(jpaRepository, mapper);
        this.walletEntityRepository = walletEntityRepository;
        this.walletCommandEntityRepository = walletCommandEntityRepository;
        this.walletCommandEntityMapper = walletCommandEntityMapper;
        this.walletHistoryEntityRepository = walletHistoryEntityRepository;
        this.walletHistoryEntityMapper = walletHistoryEntityMapper;
    }

    @Override
    public Wallet getById(UUID id) {
        return this.mapper.toDomain(this.walletEntityRepository.findById(id)
                .orElseThrow(() -> new ResponseException(NotFoundError.WALLET_NOT_FOUND))).enrich();
    }

    @Override
    public Wallet getByUserId(UUID userId) {
        Wallet wallet = new Wallet(userId);
        Optional<WalletEntity> found = this.walletEntityRepository.findByUserId(userId);
        if (found.isPresent()) {
            wallet = this.mapper.toDomain(found.get());
        }
        return wallet.enrich();
    }

    @Override
    public Wallet getWalletByUserIdAndSelectedCommand(UUID userId, UUID commandId) {
        Wallet wallet = this.getByUserId(userId);
        WalletCommand command = this.walletCommandEntityMapper.toDomain(this.walletCommandEntityRepository
                .findById(commandId).orElseThrow(() -> new ResponseException(NotFoundError.WALLET_COMMAND_NOTFOUND)));
        return wallet.enrich(command);
    }

    @Override
    @Transactional
    public List<Wallet> saveAll(List<Wallet> domains) {
        List<WalletHistory> histories = new ArrayList<>();
        List<WalletCommand> commands = new ArrayList<>();
        domains.forEach(domain -> {
            if (!CollectionUtils.isEmpty(domain.getHistories())) {
                histories.addAll(domain.getHistories());
            }
            if (!CollectionUtils.isEmpty(domain.getCommands())) {
                commands.addAll(domain.getCommands());
            }
        });
        this.walletCommandEntityRepository.saveAll(this.walletCommandEntityMapper.toEntity(commands));
        this.walletHistoryEntityRepository.saveAll(this.walletHistoryEntityMapper.toEntity(histories));
        return super.saveAll(domains);
    }
}
