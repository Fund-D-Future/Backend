package com.funddfuture.fund_d_future.donation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/donations")
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;

    // POST /donations: Create a new donation
    @PostMapping
    public ResponseEntity<?> save(@RequestBody DonationRequest request) {
        donationService.save(request);
        return ResponseEntity.accepted().build();
    }

    // GET /donations/campaign/{campaignId}: Get donations for a specific campaign
    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<Donation>> findByCampaignId(@PathVariable UUID campaignId) {
        return ResponseEntity.ok(donationService.findByCampaignId(campaignId));
    }

    // GET /donations/donor/{donorId}: Get donations made by a specific donor
    @GetMapping("/donor/{donorId}")
    public ResponseEntity<List<Donation>> findByDonorId(@PathVariable UUID donorId) {
        return ResponseEntity.ok(donationService.findByDonorId(donorId));
    }
}