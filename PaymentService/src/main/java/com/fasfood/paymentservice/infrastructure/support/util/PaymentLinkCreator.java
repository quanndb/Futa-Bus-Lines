package com.fasfood.paymentservice.infrastructure.support.util;

import com.fasfood.paymentservice.domain.WalletCommand;
import com.fasfood.paymentservice.infrastructure.support.enums.WalletAction;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.payos.PayOS;
import vn.payos.type.PaymentData;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class PaymentLinkCreator {
    @Value(value = "${app.bank.sepay-qr-url}")
    private String sePayQrUrl;
    @Value(value = "${app.payos.return-url}")
    private String RETURN_URL;
    private final PayOS payOS;

    public String create(WalletCommand cmd) {
        if (WalletAction.WITH_DRAW.equals(cmd.getAction())) {
            return this.createWithdrawLink(cmd);
        }
        if (WalletAction.DEPOSIT.equals(cmd.getAction()) || WalletAction.USING.equals(cmd.getAction())) {
            return this.generatePayOSLink(cmd);
        }
        return null;
    }

    public String generatePayOSLink(WalletCommand command) {
        try {
            return this.payOS.createPaymentLink(PaymentData.builder()
                    .orderCode((long) Math.abs(new Random().nextInt()))
                    .amount((int) command.getAmount())
                    .description(command.getCode())
                    .returnUrl(this.RETURN_URL)
                    .cancelUrl(this.RETURN_URL)
                    .build()).getCheckoutUrl();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String createWithdrawLink(WalletCommand cmd) {
        if (WalletAction.WITH_DRAW.equals(cmd.getAction())) {
            return String.format("%s?acc=%s&bank=%s&amount=%s&des=%s", this.sePayQrUrl, cmd.getAccountNumber(), cmd.getBankCode(), cmd.getAmount(), cmd.getCode());
        }
        return null;
    }
}
