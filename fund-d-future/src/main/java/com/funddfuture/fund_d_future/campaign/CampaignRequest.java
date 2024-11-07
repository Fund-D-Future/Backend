package com.funddfuture.fund_d_future.campaign;

import com.funddfuture.fund_d_future.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
public class CampaignRequest {

    private UUID id;
    private String name;
    private String description;
    private Double fundingGoal;
    private String currency;
    private Instant startDate;
    private Instant endDate;
    private User owner;
}