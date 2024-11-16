package com.funddfuture.fund_d_future.reward;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rewards")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    // POST /rewards: Create a new reward
    @PostMapping
    public ResponseEntity<?> save(@RequestBody RewardRequest request) {
        rewardService.save(request);
        return ResponseEntity.accepted().build();
    }

    // GET /rewards/campaign/{campaignId}: Get rewards for a specific campaign
    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<Reward>> findByCampaignId(@PathVariable UUID campaignId) {
        return ResponseEntity.ok(rewardService.findByCampaignId(campaignId));
    }

    // PUT /rewards/{id}: Update a reward
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody RewardRequest request) {
        rewardService.update(id, request);
        return ResponseEntity.ok().build();
    }

    // DELETE /rewards/{id}: Delete a reward
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        rewardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}