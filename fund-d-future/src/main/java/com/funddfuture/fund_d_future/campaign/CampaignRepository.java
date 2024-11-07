package com.funddfuture.fund_d_future.campaign;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CampaignRepository extends JpaRepository<Campaign, UUID> {
    @Query("SELECT c FROM Campaign c WHERE c.id = :ownerId")
    List<Campaign> findByOwnerId(@Param("ownerId") UUID ownerId);
}