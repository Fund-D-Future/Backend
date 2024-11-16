package com.funddfuture.fund_d_future.auth;

import com.funddfuture.fund_d_future.user.CountryList;
import com.funddfuture.fund_d_future.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String firstname;
  private String lastname;

  private String email;

  private String password;
  private String confirmPassword;
  private Role role;
  private CountryList residentCountry;
}
