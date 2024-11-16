package com.funddfuture.fund_d_future.ai;

import com.funddfuture.fund_d_future.campaign.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AIPoweredRepository extends JpaRepository<Campaign, UUID> {

    @Query("SELECT c FROM Campaign c WHERE c.owner.id = :userId")
    List<Campaign> findRecommendedCampaigns(@Param("userId") UUID userId);
}