package com.funddfuture.fund_d_future.reward;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final RewardRepository repository;

    public void save(RewardRequest request) {
        var reward = Reward.builder()
                .id(request.getId())
                .name(request.getName())
                .description(request.getDescription())
                .amount(request.getAmount())
                .campaign(request.getCampaign())
                .build();
        repository.save(reward);
    }

    public List<Reward> findByCampaignId(UUID campaignId) {
        return repository.findByCampaignId(campaignId);
    }

    public void update(UUID id, RewardRequest request) {
        var reward = repository.findById(id).orElseThrow(() -> new RuntimeException("Reward not found"));
        reward.setName(request.getName());
        reward.setDescription(request.getDescription());
        reward.setAmount(request.getAmount());
        reward.setCampaign(request.getCampaign());
        repository.save(reward);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}