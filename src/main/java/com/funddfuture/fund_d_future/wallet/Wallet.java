package com.funddfuture.fund_d_future.wallet;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.funddfuture.fund_d_future.campaign.Campaign;
import com.funddfuture.fund_d_future.campaign.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, columnDefinition = "DOUBLE PRECISION DEFAULT 0")
    private Double balance;

    @Column(nullable = false)
    private Currency currency;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    @JsonBackReference
    private Campaign campaign;


    }