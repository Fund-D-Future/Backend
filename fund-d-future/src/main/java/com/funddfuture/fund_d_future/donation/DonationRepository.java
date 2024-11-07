package com.funddfuture.fund_d_future.donation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DonationRepository extends JpaRepository<Donation, UUID> {

    @Query("SELECT d FROM Donation d WHERE d.campaign.id = :campaignId")
    List<Donation> findByCampaignId(@Param("campaignId") UUID campaignId);

    @Query("SELECT d FROM Donation d WHERE d.donor.id = :donorId")
    List<Donation> findByDonorId(@Param("donorId") UUID donorId);
}