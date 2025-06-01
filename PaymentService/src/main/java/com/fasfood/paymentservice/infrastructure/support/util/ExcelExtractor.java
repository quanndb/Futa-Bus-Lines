package com.fasfood.paymentservice.infrastructure.support.util;

import com.fasfood.commonexcel.CellConverter;
import com.fasfood.commonexcel.ExcelUtil;
import com.fasfood.paymentservice.application.dto.response.TransactionDTO;
import com.fasfood.paymentservice.application.dto.response.WalletCommandDTO;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletCommandStatus;

public class ExcelExtractor {
    public static ExcelUtil.ExcelMapper<TransactionDTO> extractTransactionOut() {
        return ExcelUtil.mapperFor(TransactionDTO.class)
                .addColumn("Id", CellConverter.longConverter(),
                        "id", TransactionDTO::setId, TransactionDTO::getId)
                .addColumn("Kênh thanh toán", CellConverter.stringConverter(),
                        "gateway", TransactionDTO::setGateway, TransactionDTO::getGateway)
                .addColumn("Ngày giao dịch", CellConverter.localDateTimeConverter(),
                        "transactionDate", TransactionDTO::setTransactionDate, TransactionDTO::getTransactionDate)
                .addColumn("Số tài khoản", CellConverter.stringConverter(),
                        "accountNumber", TransactionDTO::setAccountNumber, TransactionDTO::getAccountNumber)
                .addColumn("Mã lệnh", CellConverter.stringConverter(),
                        "code", TransactionDTO::setCode, TransactionDTO::getCode)
                .addColumn("Nội dung", CellConverter.stringConverter(),
                        "content", TransactionDTO::setContent, TransactionDTO::getContent)
                .addColumn("Số tiền", CellConverter.longConverter(),
                        "transferAmount", TransactionDTO::setTransferAmount, TransactionDTO::getTransferAmount)
                .addColumn("Mã tham chiếu", CellConverter.stringConverter(),
                        "referenceCode", TransactionDTO::setReferenceCode, TransactionDTO::getReferenceCode)
                .addColumn("Mô tả", CellConverter.stringConverter(),
                        "description", TransactionDTO::setDescription, TransactionDTO::getDescription);
    }

    public static ExcelUtil.ExcelMapper<WalletCommandDTO> extractWithdrawal() {
        return ExcelUtil.mapperFor(WalletCommandDTO.class)
                .addColumn("Mã lệnh", CellConverter.stringConverter(),
                        "code", WalletCommandDTO::setCode, WalletCommandDTO::getCode)
                .addColumn("Kênh thanh toán", CellConverter.stringConverter(),
                        "bankCode", WalletCommandDTO::setBankCode, WalletCommandDTO::getBankCode)
                .addColumn("Số tài khoản", CellConverter.stringConverter(),
                        "accountNumber", WalletCommandDTO::setAccountNumber, WalletCommandDTO::getAccountNumber)
                .addColumn("Người nhận", CellConverter.stringConverter(),
                        "receiverName", WalletCommandDTO::setReceiverName, WalletCommandDTO::getReceiverName)
                .addColumn("Số tiền", CellConverter.longConverter(),
                        "amount", WalletCommandDTO::setAmount, WalletCommandDTO::getAmount)
                .addColumn("Trạng thái", CellConverter.enumConverter(WalletCommandStatus.class),
                        "status", WalletCommandDTO::setStatus, WalletCommandDTO::getStatus)
                .addColumn("Ngày hoàn thành", CellConverter.instantConverter(),
                        "completedAt", WalletCommandDTO::setCompletedAt, WalletCommandDTO::getCompletedAt);
    }
}
