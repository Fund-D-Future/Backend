package com.funddfuture.fund_d_future.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    @Query("SELECT w FROM Wallet w WHERE w.campaign.id = :campaignId")
    Optional<Wallet> findByCampaignId(UUID campaignId);
}