package com.funddfuture.fund_d_future.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompletePaymentRequest {
    private PaymentRequest paymentRequest;
    private String otp;
}