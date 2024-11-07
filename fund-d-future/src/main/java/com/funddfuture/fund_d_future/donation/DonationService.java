package com.funddfuture.fund_d_future.donation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository repository;

    public void save(DonationRequest request) {
        var donation = Donation.builder()
                .id(request.getId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .campaign(request.getCampaign())
                .donor(request.getDonor())
                .build();
        repository.save(donation);
    }

    public List<Donation> findByCampaignId(UUID campaignId) {
        return repository.findByCampaignId(campaignId);
    }

    public List<Donation> findByDonorId(UUID donorId) {
        return repository.findByDonorId(donorId);
    }
}