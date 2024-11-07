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

    private UUID id;
    private Double amount;
    private String currency;
    private Campaign campaign;
    private User donor;
}