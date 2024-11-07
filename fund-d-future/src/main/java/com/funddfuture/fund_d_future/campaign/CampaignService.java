package com.funddfuture.fund_d_future.campaign;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository repository;

    public void save(CampaignRequest request) {
        var campaign = Campaign.builder()
                .id(request.getId())
                .name(request.getName())
                .description(request.getDescription())
                .fundingGoal(request.getFundingGoal())
                .currency(request.getCurrency())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .owner(request.getOwner())
                .build();
        repository.save(campaign);
    }

    public List<Campaign> findAll() {
        return repository.findAll();
    }

    public Campaign findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Campaign not found"));
    }

    public void update(UUID id, CampaignRequest request) {
        var campaign = repository.findById(id).orElseThrow(() -> new RuntimeException("Campaign not found"));
        campaign.setName(request.getName());
        campaign.setDescription(request.getDescription());
        campaign.setFundingGoal(request.getFundingGoal());
        campaign.setCurrency(request.getCurrency());
        campaign.setStartDate(request.getStartDate());
        campaign.setEndDate(request.getEndDate());
        campaign.setOwner(request.getOwner());
        repository.save(campaign);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public List<Campaign> findByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId);
    }

    public List<Campaign> findRecommended() {
        // Implement your recommendation logic here
        return repository.findAll(); // Placeholder
    }
}