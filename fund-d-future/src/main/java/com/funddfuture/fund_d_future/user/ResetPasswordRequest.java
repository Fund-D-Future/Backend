package com.funddfuture.fund_d_future.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordRequest {
    private String password;
    private String confirmPassword;
    private String email;


    public ResetPasswordRequest(String password, String confirmPassword, String email) {
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
    }
}
