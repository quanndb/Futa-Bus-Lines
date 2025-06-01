package com.fasfood.bookingservice.infrastructure.support.exception;

import com.fasfood.common.error.ResponseError;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BadRequestError implements ResponseError {
    TRIP_REQUIRED(4000001, "Trip is required"),
    DEPARTURE_TRIP_REQUIRED(4000002, "Departure trip is required"),
    DEPARTURE_REQUIRED(4000003, "Departure is required"),
    DESTINATION_REQUIRED(4000004, "Destination is required"),
    DEPARTURE_DATE_REQUIRED(4000005, "Departure date is required"),
    FULL_NAME_REQUIRED(4000006, "Full name is required"),
    EMAIL_REQUIRED(4000007, "Email is required"),
    EMAIL_NOT_VALID(4000008, "Email is not valid"),
    PHONE_NUMBER_REQUIRED(4000009, "Phone number is required"),
    PHONE_NUMBER_NOT_VALID(4000010, "Phone number is not valid"),
    SEATS_REQUIRED(4000011, "Seats is required"),
    UNABLE_TO_CHANGE_BOOKING_STATUS(4000012, "Unable to change booking status"),
    UNABLE_TO_RETURN_TICKET(4000013, "Không thể trả vé trước 24h khởi hành"),
    UNABLE_TO_UPDATE_WHILE_RUNNING(4000014, "Unable to update when running"),
    INVALID_SEATS(4000015, "Invalid seats: {0}"),
    YOU_ARE_NOT_OWNER_OF_THIS(4000016, "Bạn không phải chủ sở hữu vé này"),
    EXISTED_PAYMENT(4000017, "Đã tồn tại mã thanh toán cho đơn hàng này {0}"),
    OUT_OF_PAY(4000018, "Đơn hàng này đã quá hạn thanh toán"),
    INVALID_TRIP_DETAILS(4000019, "Chuyến xe không hợp lệ"),
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