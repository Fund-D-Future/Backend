package com.funddfuture.fund_d_future.user;

import com.funddfuture.fund_d_future.campaign.Campaign;
import com.funddfuture.fund_d_future.token.Token;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String firstname;

  private String lastname;

  private String bio;

  private String email;

  @JsonIgnore
  private String password;

  private String institution;

  @Enumerated(EnumType.STRING)
  private DegreeProgram degreeProgram;

  private String courseOfStudy;

  private int yearOfStudy;

  private String grade;

  private String proof;

  private List<String> shortTermGoals;
  private List<String> longTermGoals;
  private String extraCurricularActivities;
  private String volunteerWork;

  @Enumerated(EnumType.STRING)
  private CountryList residentCountry;

  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToMany(mappedBy = "user")
  @JsonIgnore
  private List<Token> tokens;

  @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
  private List<Campaign> campaigns;

  private String resetPasswordToken;
  private Date resetPasswordExpires;
  private String transactionPin;

  @Override
  @JsonIgnore
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}