package com.funddfuture.fund_d_future.token;

import com.funddfuture.fund_d_future.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  public UUID id;

  @Column(unique = true)
  public String token;

  @Enumerated(EnumType.STRING)
  public TokenType tokenType = TokenType.BEARER;

  public boolean revoked;

  public boolean expired;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @JsonBackReference
  public User user;

  // Token issued at time
  public LocalDateTime issuedAt;

  // Token expiration time
  public LocalDateTime expiresAt;

  // Token revoked time
  public LocalDateTime revokedAt;

  // Token revoked by
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "revoked_by_user_id")
  @JsonBackReference
  public User revokedBy;
}