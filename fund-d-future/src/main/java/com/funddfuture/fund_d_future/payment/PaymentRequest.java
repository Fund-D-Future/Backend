package com.funddfuture.fund_d_future.payment;

import com.funddfuture.fund_d_future.campaign.Campaign;
import com.funddfuture.fund_d_future.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class PaymentRequest {
    private Double amount;
    private String currency;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private String transactionReference;
    private String description;
    private CardDetails card;
    private String callbackUrl;
    private String token;
}