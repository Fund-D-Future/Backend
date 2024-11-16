package com.funddfuture.fund_d_future.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    @Query("SELECT p FROM Payment p WHERE p.donor.id = :donorId")
    List<Payment> findByDonorId(@Param("donorId") UUID donorId);

    @Query("SELECT p FROM Payment p WHERE p.campaign.id = :campaignId")
    List<Payment> findByCampaignId(@Param("campaignId") UUID campaignId);
}