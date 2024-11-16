package com.funddfuture.fund_d_future.campaign;


public class SimilarityResult {
    private final Campaign campaign;
    private final double similarity;

    public SimilarityResult(Campaign campaign, double similarity) {
        this.campaign = campaign;
        this.similarity = similarity;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public double getSimilarity() {
        return similarity;
    }
}


