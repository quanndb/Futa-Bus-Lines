package com.fasfood.paymentservice.infrastructure.support.exception;

import com.fasfood.common.error.ResponseError;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BadRequestError implements ResponseError {
    UNABLE_TO_RESOLVE(4000001, "Không thể xử lý lệnh này"),
    INSUFFICIENT_BALANCE(4000002, "Số dư không hợp lệ"),
    UNABLE_TO_CREATE_COMMAND(4000003, "Không thể tạo lệnh này"),
    UNKNOWN_ACTION(4000004, "Không rõ hành động"),
    BANK_CODE_REQUIRED(4000005, "Mã ngân hàng không được bỏ trống"),
    ACCOUNT_NUMBER_REQUIRED(4000006, "Số tài khoản không được bỏ trống"),
    RECEIVER_NAME_REQUIRED(4000007, "Tên người nhận không được bỏ trống"),
    AMOUNT_REQUIRED(4000008, "Số tiền không được bỏ trống"),
    AMOUNT_AT_LEAST(4000009, "Số tiền ít nhất là 10k"),
    CONTENT_REQUIRED(4000010, "Nội dung không được bỏ trống"),
    ACTION_REQUIRED(4000011, "Hành động không được để trống"),
    INVALID_TRANSFER_AMOUNT(4000012, "Số tiền chuyển không chính xác"),
    PAYMENT_LINK_AT_LEAST(4000013, "Số tiền tạo thanh toán ít nhất 10k"),
    UNABLE_TO_RETURN_BEFORE(4000014, "Không thể hoàn trả sau 24h"),
    ORDER_CODE_REQUIRED(4000015, "Mã đơn hàng không được bỏ trống"),
    IS_USE_WALLET_REQUIRED(4000016, "Lựa chọn sử dụng ví hay không không được bỏ trống"),
    USER_ID_REQUIRED(4000017, "User id không được bỏ trống"),
    EXISTED_ORDER_CODE(4000018, "Mã đơn hàng đã tồn tại"),
    INVALID_ORDER_CODE(400019, "Mã đơn hàng không hợp lệ")
    ;


    private final Integer code;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public int getStatus() {
        return 400;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
