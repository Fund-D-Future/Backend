package com.funddfuture.fund_d_future.auth;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Date;
import java.util.UUID;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auth")
public class AuthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String username;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String resetPasswordToken;

    private Date resetPasswordTokenExpiryDate;

    private Long createdAt;

    private Long updatedAt;


    public AuthEntity(String username,
                      String password,
                      String email,
                      Role role,
                      String resetPasswordToken,
                      Date resetPasswordTokenExpiryDate,
                      Long createdAt,
                      Long updatedAt) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.resetPasswordToken = resetPasswordToken;
        this.role = role;
        this.resetPasswordTokenExpiryDate = resetPasswordTokenExpiryDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}