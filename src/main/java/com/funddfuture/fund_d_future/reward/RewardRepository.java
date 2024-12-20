package com.funddfuture.fund_d_future.reward;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RewardRepository extends JpaRepository<Reward, UUID> {

    @Query("SELECT r FROM Reward r WHERE r.campaign.id = :campaignId")
    List<Reward> findByCampaignId(@Param("campaignId") UUID campaignId);
}