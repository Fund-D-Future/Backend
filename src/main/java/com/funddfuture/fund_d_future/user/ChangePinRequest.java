package com.funddfuture.fund_d_future.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePinRequest {
    private String oldPin;
    private String newPin;
}