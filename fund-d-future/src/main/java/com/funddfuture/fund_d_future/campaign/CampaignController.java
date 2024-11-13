package com.funddfuture.fund_d_future.campaign;

import com.funddfuture.fund_d_future.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody CampaignRequest request) {
        campaignService.save(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<List<Campaign>> findAll() {
        return ResponseEntity.ok(campaignService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Campaign> findById(@PathVariable UUID id) {
        Campaign campaign = campaignService.findById(id);
        if (campaign == null) {
            throw new NotFoundException("Campaign not found");
        }
        return ResponseEntity.ok(campaign);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody CampaignRequest request) {
        try {
            campaignService.update(id, request);
        } catch (Exception e) {
            throw new NotFoundException("Campaign not found");
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            campaignService.delete(id);
        } catch (Exception e) {
            throw new NotFoundException("Campaign not found");
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/end")
    public ResponseEntity<?> endCampaign(@PathVariable UUID id) {
        try {
            campaignService.endCampaign(id);
        } catch (Exception e) {
            throw new NotFoundException("Campaign not found");
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Campaign>> findByOwnerId(@PathVariable UUID ownerId) {
        List<Campaign> campaigns = campaignService.findByOwnerId(ownerId);
        if (campaigns.isEmpty()) {
            throw new NotFoundException("No campaigns found for the owner");
        }
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/vector-search")
    public ResponseEntity<List<Campaign>> vectorSearch(@RequestParam("query") String searchQuery) {
        List<Campaign> campaigns = campaignService.vectorSearch(searchQuery);
        if (campaigns.isEmpty()) {
            throw new NotFoundException("No campaigns found matching the search query");
        }
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/feature")
    public ResponseEntity<List<Campaign>> findByFeature(@RequestParam("feature") CampaignFeature feature) {
        List<Campaign> campaigns = campaignService.findByFeature(feature);
        if (campaigns.isEmpty()) {
            throw new NotFoundException("No campaigns found for the selected feature");
        }
        return ResponseEntity.ok(campaigns);
    }
}
