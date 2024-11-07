package com.funddfuture.fund_d_future.campaign;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CampaignRepository extends JpaRepository<Campaign, UUID> {
    List<Campaign> findByOwnerId(UUID ownerId);
}