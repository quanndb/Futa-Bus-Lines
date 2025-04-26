package com.fasfood.paymentservice.application.mapper;

import com.fasfood.common.dto.request.WebhookRequest;
import com.fasfood.common.mapper.CmdMapper;
import com.fasfood.paymentservice.application.dto.request.DepositRequest;
import com.fasfood.common.dto.request.PayRequest;
import com.fasfood.paymentservice.application.dto.request.WithDrawCreateOrUpdateRequest;
import com.fasfood.paymentservice.domain.cmd.DepositCmd;
import com.fasfood.paymentservice.domain.cmd.PayCmd;
import com.fasfood.paymentservice.domain.cmd.WebHookCmd;
import com.fasfood.paymentservice.domain.cmd.WithdrawCreateOrUpdateCmd;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentCommandMapper extends CmdMapper<WithdrawCreateOrUpdateCmd, WithDrawCreateOrUpdateRequest> {
    WebHookCmd from(WebhookRequest request);

    PayCmd from(PayRequest request);

    DepositCmd from(DepositRequest request);
}
