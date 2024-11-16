package com.funddfuture.fund_d_future.ai;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CampaignRecommendationRequest {

    private UUID userId;
}