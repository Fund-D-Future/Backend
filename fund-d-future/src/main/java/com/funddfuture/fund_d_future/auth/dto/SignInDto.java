package com.funddfuture.fund_d_future.auth.dto;
import lombok.Data;

@Data
public class SignInDto {
    private String email;
    private String password;
}