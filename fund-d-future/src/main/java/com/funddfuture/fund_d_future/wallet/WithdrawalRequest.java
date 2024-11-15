package com.funddfuture.fund_d_future.wallet;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WithdrawalRequest {
    private Double amount;
    private String accountNumber;
    private String accountName;
    private String bankCode;
    private String transactionPin;
}