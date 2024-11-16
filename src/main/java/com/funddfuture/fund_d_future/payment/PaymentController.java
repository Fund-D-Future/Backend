package com.funddfuture.fund_d_future.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/card/{campaignId}/initiate")
    public ResponseEntity<String> initiateCardPayment(@PathVariable UUID campaignId, @RequestBody PaymentRequest request) {
        try {
            String response = paymentService.processCardPayment(request, campaignId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/card/{campaignId}/complete")
    public ResponseEntity<String> completeCardPayment(@PathVariable UUID campaignId, @RequestBody CompletePaymentRequest request) {
        try {
            String response = paymentService.completeCardPayment(request.getPaymentRequest(), request.getOtp(), campaignId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }


    // GET /payments/donor/{donorId}: Get payments made by a specific donor
    @GetMapping("/donor/{donorId}")
    public ResponseEntity<List<Payment>> findByDonorId(@PathVariable UUID donorId) {
        return ResponseEntity.ok(paymentService.findByDonorId(donorId));
    }

    // GET /payments/campaign/{campaignId}: Get payments made to a specific campaign
    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<Payment>> findByCampaignId(@PathVariable UUID campaignId) {
        return ResponseEntity.ok(paymentService.findByCampaignId(campaignId));
    }
}