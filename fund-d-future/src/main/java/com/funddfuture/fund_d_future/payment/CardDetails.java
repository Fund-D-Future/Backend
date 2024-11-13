package com.funddfuture.fund_d_future.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CardDetails {
    private String expiryMonth;
    private String expiryYear;
    private String securityCode;
    private String cardNumber;
}