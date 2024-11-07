package com.funddfuture.fund_d_future.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;

    public void process(PaymentRequest request) {
        var payment = Payment.builder()
                .id(request.getId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .campaign(request.getCampaign())
                .donor(request.getDonor())
                .build();
        repository.save(payment);
    }

    public List<Payment> findByDonorId(UUID donorId) {
        return repository.findByDonorId(donorId);
    }

    public List<Payment> findByCampaignId(UUID campaignId) {
        return repository.findByCampaignId(campaignId);
    }
}