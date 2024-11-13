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

    @PostMapping("/card")
    public ResponseEntity<String> processCardPayment(@RequestBody PaymentRequest request) {
        try {
            String response = paymentService.processCardPayment(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // POST /payments: Process a new payment
    @PostMapping
    public ResponseEntity<?> process(@RequestBody PaymentRequest request) {
        paymentService.processPayment(request);
        return ResponseEntity.accepted().build();
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