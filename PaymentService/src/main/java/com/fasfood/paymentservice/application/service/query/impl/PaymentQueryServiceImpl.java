package com.fasfood.paymentservice.application.service.query.impl;

import com.fasfood.common.dto.PageDTO;
import com.fasfood.common.dto.response.StatisticResponse;
import com.fasfood.common.exception.ResponseException;
import com.fasfood.paymentservice.application.dto.mapper.TransactionDTOMapper;
import com.fasfood.paymentservice.application.dto.mapper.WalletCommandDTOMapper;
import com.fasfood.paymentservice.application.dto.mapper.WalletDTOMapper;
import com.fasfood.paymentservice.application.dto.mapper.WalletHistoryDTOMapper;
import com.fasfood.paymentservice.application.dto.request.TransactionPagingRequest;
import com.fasfood.paymentservice.application.dto.request.WalletCommandPagingRequest;
import com.fasfood.paymentservice.application.dto.request.WalletHistoryPagingRequest;
import com.fasfood.paymentservice.application.dto.response.TransactionDTO;
import com.fasfood.paymentservice.application.dto.response.WalletCommandDTO;
import com.fasfood.paymentservice.application.dto.response.WalletDTO;
import com.fasfood.paymentservice.application.dto.response.WalletHistoryDTO;
import com.fasfood.paymentservice.application.mapper.PaymentQueryMapper;
import com.fasfood.paymentservice.application.service.query.PaymentQueryService;
import com.fasfood.paymentservice.domain.query.TransactionPagingQuery;
import com.fasfood.paymentservice.domain.query.WalletCommandPagingQuery;
import com.fasfood.paymentservice.domain.query.WalletHistoryPagingQuery;
import com.fasfood.paymentservice.domain.repository.WalletRepository;
import com.fasfood.paymentservice.infrastructure.persistence.repository.TransactionEntityRepository;
import com.fasfood.paymentservice.infrastructure.persistence.repository.WalletCommandEntityRepository;
import com.fasfood.paymentservice.infrastructure.persistence.repository.WalletHistoryEntityRepository;
import com.fasfood.paymentservice.infrastructure.support.exception.NotFoundError;
import com.fasfood.paymentservice.infrastructure.support.util.PaymentLinkCreator;
import com.fasfood.web.support.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentQueryServiceImpl implements PaymentQueryService {

    private final WalletRepository walletRepository;
    private final WalletDTOMapper walletDTOMapper;
    private final PaymentQueryMapper queryMapper;
    private final WalletHistoryEntityRepository walletHistoryEntityRepository;
    private final WalletHistoryDTOMapper walletHistoryDTOMapper;
    private final WalletCommandEntityRepository walletCommandEntityRepository;
    private final WalletCommandDTOMapper walletCommandDTOMapper;
    private final TransactionEntityRepository transactionEntityRepository;
    private final TransactionDTOMapper transactionDTOMapper;
    private final PaymentLinkCreator paymentLinkEnrichTor;

    @Override
    public WalletDTO getMyWallet() {
        return this.walletDTOMapper.domainToDTO(this.walletRepository.getByUserId(SecurityUtils.getUserId()));
    }

    @Override
    public PageDTO<WalletHistoryDTO> getMyWalletHistories(WalletHistoryPagingRequest request) {
        WalletHistoryPagingQuery query = this.queryMapper.from(request);
        query.setUserId(SecurityUtils.getUserId());
        return this.getWalletHistories(query);
    }

    @Override
    public PageDTO<WalletHistoryDTO> getHistories(WalletHistoryPagingRequest request) {
        return this.getWalletHistories(this.queryMapper.from(request));
    }

    @Override
    public PageDTO<WalletCommandDTO> getMyWalletCommands(WalletCommandPagingRequest request) {
        WalletCommandPagingQuery query = this.queryMapper.from(request);
        query.setUserId(SecurityUtils.getUserId());
        return this.getWalletCommands(query);
    }

    @Override
    public PageDTO<WalletCommandDTO> getCommands(WalletCommandPagingRequest request) {
        return this.getWalletCommands(this.queryMapper.from(request));
    }

    @Override
    public WalletCommandDTO getById(UUID id) {
        return this.walletCommandDTOMapper.entityToDTO(this.walletCommandEntityRepository.findById(id)
                .orElseThrow(() -> new ResponseException(NotFoundError.WALLET_COMMAND_NOTFOUND, id.toString())));
    }

    @Override
    public PageDTO<TransactionDTO> getTransactions(TransactionPagingRequest request) {
        return this.getTransactions(this.queryMapper.from(request));
    }

    @Override
    public StatisticResponse getStatistic(TransactionPagingRequest request) {
        var res = this.transactionEntityRepository.statistics(this.queryMapper.from(request));
        if (!CollectionUtils.isEmpty(res)) return res.getFirst();
        return new StatisticResponse("total", 0L);
    }

    @Override
    public StatisticResponse getStatistic(WalletCommandPagingRequest request) {
        var res = this.walletCommandEntityRepository.statistics(this.queryMapper.from(request));
        if (!CollectionUtils.isEmpty(res)) return res.getFirst();
        return new StatisticResponse("total", 0L);
    }

    private PageDTO<WalletHistoryDTO> getWalletHistories(WalletHistoryPagingQuery query) {
        long count = this.walletHistoryEntityRepository.count(query);
        if (count == 0) return PageDTO.empty();
        List<WalletHistoryDTO> dto = this.walletHistoryDTOMapper.entityToDTO(this.walletHistoryEntityRepository.search(query));
        return PageDTO.of(dto, query.getPageIndex(), query.getPageSize(), count);
    }

    private PageDTO<WalletCommandDTO> getWalletCommands(WalletCommandPagingQuery query) {
        long count = this.walletCommandEntityRepository.count(query);
        if (count == 0) return PageDTO.empty();
        List<WalletCommandDTO> dto = this.walletCommandDTOMapper.entityToDTO(this.walletCommandEntityRepository.search(query));
        return PageDTO.of(dto, query.getPageIndex(), query.getPageSize(), count);
    }

    private PageDTO<TransactionDTO> getTransactions(TransactionPagingQuery query) {
        long count = this.transactionEntityRepository.count(query);
        if (count == 0) return PageDTO.empty();
        List<TransactionDTO> dto = this.transactionDTOMapper.entityToDTO(this.transactionEntityRepository.search(query));
        return PageDTO.of(dto, query.getPageIndex(), query.getPageSize(), count);
    }
}
