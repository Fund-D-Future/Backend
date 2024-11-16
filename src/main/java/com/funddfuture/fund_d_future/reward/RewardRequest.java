package com.funddfuture.fund_d_future.reward;

import com.funddfuture.fund_d_future.campaign.Campaign;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class RewardRequest {

    private UUID id;
    private String name;
    private String description;
    private Double amount;
    private Campaign campaign;
}