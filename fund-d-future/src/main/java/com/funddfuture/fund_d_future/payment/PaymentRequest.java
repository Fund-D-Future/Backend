package com.funddfuture.fund_d_future.payment;

import com.funddfuture.fund_d_future.campaign.Campaign;
import com.funddfuture.fund_d_future.campaign.Currency;
import com.funddfuture.fund_d_future.user.User;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class PaymentRequest {
    private Double amount;
    private Currency currency;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private String description;
    private CardDetails card;
}