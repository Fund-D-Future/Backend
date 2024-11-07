package com.funddfuture.fund_d_future.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AIPoweredController {

    private final AIPoweredService aiPoweredService;

    // POST /campaigns/analyze: Analyze a campaign using sentiment analysis
    @PostMapping("/campaigns/analyze")
    public ResponseEntity<?> analyzeCampaign(@RequestBody CampaignAnalysisRequest request) {
        aiPoweredService.analyzeCampaign(request);
        return ResponseEntity.accepted().build();
    }

    // POST /campaigns/recommend: Get recommended campaigns for a user
    @PostMapping("/campaigns/recommend")
    public ResponseEntity<?> recommendCampaigns(@RequestBody CampaignRecommendationRequest request) {
        return ResponseEntity.ok(aiPoweredService.recommendCampaigns(request));
    }

    // POST /donations/fraud-detection: Detect potentially fraudulent donations
    @PostMapping("/donations/fraud-detection")
    public ResponseEntity<?> detectFraud(@RequestBody FraudDetectionRequest request) {
        aiPoweredService.detectFraud(request);
        return ResponseEntity.accepted().build();
    }
}