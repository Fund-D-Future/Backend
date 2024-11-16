package com.funddfuture.fund_d_future.campaign;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.funddfuture.fund_d_future.campaign.CampaignFeature;
import com.funddfuture.fund_d_future.campaign.Currency;
import com.funddfuture.fund_d_future.file.File;
import com.funddfuture.fund_d_future.user.User;
import com.funddfuture.fund_d_future.wallet.Wallet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double fundingGoal;

    @Column(name = "raised_funding", columnDefinition = "DOUBLE PRECISION DEFAULT 0", nullable = false)
    private Double raisedFunding;

    @Column(name = "embedding")
    @JsonIgnore
    private float[] embedding;

    @Column(nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CampaignFeature feature;

    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @Column(name = "end_date", nullable = false)
    private Instant endDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"campaigns", "tokens"})
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "campaign")
    @JsonManagedReference
    private List<File> files;

    @OneToOne(mappedBy = "campaign", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Wallet wallet;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}