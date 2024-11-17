package com.funddfuture.fund_d_future.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordRequest {
    private String password;
    private String email;


    public ResetPasswordRequest(String password, String email) {
        this.password = password;
        this.email = email;
    }
}
