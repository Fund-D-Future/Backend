package com.funddfuture.fund_d_future.campaign;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CampaignRepository extends JpaRepository<Campaign, UUID> {

    // include query param to avoid sql injection
    @Query("SELECT c FROM Campaign c WHERE c.owner.id = :ownerId")
    List<Campaign> findByOwnerId(UUID ownerId);

    @Query("SELECT c FROM Campaign c WHERE c.feature = :feature")
    List<Campaign> findByFeature(CampaignFeature feature);

}