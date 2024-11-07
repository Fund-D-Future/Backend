package com.funddfuture.fund_d_future.campaign;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/campaign")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    // POST /campaigns: Create a new campaign
    @PostMapping
    public ResponseEntity<?> save(@RequestBody CampaignRequest request) {
        campaignService.save(request);
        return ResponseEntity.accepted().build();
    }

    // GET /campaigns: Get a list of all campaigns
    @GetMapping
    public ResponseEntity<List<Campaign>> findAll() {
        return ResponseEntity.ok(campaignService.findAll());
    }

    // GET /campaigns/{id}: Get a campaign by ID
    @GetMapping("/{id}")
    public ResponseEntity<Campaign> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(campaignService.findById(id));
    }

    // PUT /campaigns/{id}: Update a campaign
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody CampaignRequest request) {
        campaignService.update(id, request);
        return ResponseEntity.ok().build();
    }

    // DELETE /campaigns/{id}: Delete a campaign
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        campaignService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET /campaigns/owner/{ownerId}: Get campaigns by owner ID
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Campaign>> findByOwnerId(@PathVariable UUID ownerId) {
        return ResponseEntity.ok(campaignService.findByOwnerId(ownerId));
    }

    // GET /campaigns/recommended: Get recommended campaigns for the user
    @GetMapping("/recommended")
    public ResponseEntity<List<Campaign>> findRecommended() {
        return ResponseEntity.ok(campaignService.findRecommended());
    }
}