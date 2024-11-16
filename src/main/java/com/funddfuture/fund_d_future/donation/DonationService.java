package com.funddfuture.fund_d_future.donation;

import com.funddfuture.fund_d_future.campaign.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository repository;
    private final CampaignRepository campaignRepository;

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

    public void updateRaisedFunding(UUID campaignId, Double amount) {
        var campaignDonations = repository.findByCampaignId(campaignId);
        var raisedFunding = campaignDonations.stream()
                .mapToDouble(Donation::getAmount)
                .sum();
        System.out.println("Raised funding: " + raisedFunding);
        var campaign = campaignDonations.get(0).getCampaign();
        campaign.setRaisedFunding(raisedFunding);
        campaignRepository.save(campaign);
    }

    public List<Donation> findByCampaignId(UUID campaignId) {
        return repository.findByCampaignId(campaignId);
    }

    public List<Donation> findByDonorId(UUID donorId) {
        return repository.findByDonorId(donorId);
    }
}