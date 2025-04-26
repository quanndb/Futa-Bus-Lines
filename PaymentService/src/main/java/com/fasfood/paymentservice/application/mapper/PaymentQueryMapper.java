package com.fasfood.paymentservice.application.mapper;

import com.fasfood.paymentservice.application.dto.request.TransactionPagingRequest;
import com.fasfood.paymentservice.application.dto.request.WalletCommandPagingRequest;
import com.fasfood.paymentservice.application.dto.request.WalletHistoryPagingRequest;
import com.fasfood.paymentservice.domain.query.TransactionPagingQuery;
import com.fasfood.paymentservice.domain.query.WalletCommandPagingQuery;
import com.fasfood.paymentservice.domain.query.WalletHistoryPagingQuery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentQueryMapper {
    WalletHistoryPagingQuery from(WalletHistoryPagingRequest request);

    WalletCommandPagingQuery from(WalletCommandPagingRequest request);

    TransactionPagingQuery from(TransactionPagingRequest request);
}
