package com.funddfuture.fund_d_future.campaign;

import com.funddfuture.fund_d_future.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class CampaignRequest {

    private String name;
    private String description;
    private Double fundingGoal;
    private CampaignFeature feature;
    private Double raisedFunding;
    private Currency currency;
    private Instant startDate;
    private Instant endDate;

}