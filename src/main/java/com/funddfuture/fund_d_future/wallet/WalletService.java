package com.funddfuture.fund_d_future.wallet;

import com.funddfuture.fund_d_future.campaign.Campaign;
import com.funddfuture.fund_d_future.campaign.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final CampaignRepository campaignRepository;

    public Wallet createWalletForCampaign(UUID campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        Wallet wallet = Wallet.builder()
                .balance(0.0)
                .currency(campaign.getCurrency())
                .campaign(campaign)
                .build();

        return walletRepository.save(wallet);
    }

    public Wallet getWalletByCampaignId(UUID campaignId, UUID userId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        if (!campaign.getOwner().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        return walletRepository.findByCampaignId(campaignId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }
}