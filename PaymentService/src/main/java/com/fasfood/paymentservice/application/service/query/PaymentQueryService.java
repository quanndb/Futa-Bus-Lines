package com.fasfood.paymentservice.application.service.query;

import com.fasfood.common.dto.PageDTO;
import com.fasfood.paymentservice.application.dto.request.TransactionPagingRequest;
import com.fasfood.paymentservice.application.dto.request.WalletCommandPagingRequest;
import com.fasfood.paymentservice.application.dto.request.WalletHistoryPagingRequest;
import com.fasfood.paymentservice.application.dto.response.TransactionDTO;
import com.fasfood.paymentservice.application.dto.response.WalletCommandDTO;
import com.fasfood.paymentservice.application.dto.response.WalletDTO;
import com.fasfood.paymentservice.application.dto.response.WalletHistoryDTO;

public interface PaymentQueryService {
    WalletDTO getMyWallet();

    PageDTO<WalletHistoryDTO> getMyWalletHistories(WalletHistoryPagingRequest request);

    PageDTO<WalletHistoryDTO> getHistories(WalletHistoryPagingRequest request);

    PageDTO<WalletCommandDTO> getMyWalletCommands(WalletCommandPagingRequest request);

    PageDTO<WalletCommandDTO> getCommands(WalletCommandPagingRequest request);

    PageDTO<TransactionDTO> getTransactions(TransactionPagingRequest request);
}
