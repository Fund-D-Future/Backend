package com.funddfuture.fund_d_future.campaign;

import com.funddfuture.fund_d_future.user.User;
import com.funddfuture.fund_d_future.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import static org.springframework.ai.vectorstore.SimpleVectorStore.EmbeddingMath.cosineSimilarity;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final UserRepository userRepository;
    private static final Number SIMILARITY_THRESHOLD = 0.4;
    private final CampaignRepository repository;
    private final OpenAiEmbeddingModel aiClient;

    @Value("${spring.ai.openai.api-key:#{null}}")
    private String openAiApiKey;

    private final boolean enableAiSearch = openAiApiKey != null && !openAiApiKey.isBlank();

    private User getAuthenticatedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (User) ((UsernamePasswordAuthenticationToken) authentication).getPrincipal();
        return userRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Campaign save(CampaignRequest request) {
        User owner = getAuthenticatedUser();
        // Concatenate name and description for embedding
        String combinedText = request.getName() + " " + request.getDescription();
        float[] embeddedVector = aiClient.embed(combinedText);

        var campaign = Campaign.builder()
                .name(request.getName())
                .description(request.getDescription())
                .fundingGoal(request.getFundingGoal())
                .raisedFunding(0.0)
                .currency(request.getCurrency())
                .feature(request.getFeature())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .owner(owner)
                .embedding(embeddedVector) // Set the embedded vector
                .build();
        repository.save(campaign);
        return campaign;
    }
    public void update(UUID id, CampaignRequest request) {
        var campaign = repository.findById(id).orElseThrow(() -> new RuntimeException("Campaign not found"));

        boolean nameChanged = !Objects.equals(campaign.getName(), request.getName());
        boolean descriptionChanged = !Objects.equals(campaign.getDescription(), request.getDescription());

        // Update fields
        campaign.setName(request.getName());
        campaign.setDescription(request.getDescription());
        campaign.setFundingGoal(request.getFundingGoal());
        campaign.setCurrency(request.getCurrency());
        campaign.setFeature(request.getFeature());
        campaign.setStartDate(request.getStartDate());
        campaign.setEndDate(request.getEndDate());

        // Recalculate the embedded vector if name or description changed
        if (nameChanged || descriptionChanged) {
            String combinedText = request.getName() + " " + request.getDescription();
            float[] embeddedVector = aiClient.embed(combinedText);
            campaign.setEmbedding(embeddedVector);
        }

        repository.save(campaign);
    }


    public List<Campaign> findAll() {
        return repository.findAll();
    }

    public Campaign findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Campaign not found"));
    }

    public List<Campaign> findByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public void endCampaign(UUID campaignId) {
        var campaign = repository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));
        campaign.setEndDate(Instant.now());
        repository.save(campaign);
    }

    public List<Campaign> vectorSearch(String searchQuery) {
        try {
            // Retrieve all campaigns
            List<Campaign> campaigns = repository.findAll();

            // Generate the embedding for the search query
            float[] queryEmbedding = aiClient.embed(searchQuery);

            // Use CompletableFuture to handle asynchronous operations
            List<CompletableFuture<SimilarityResult>> similarityFutures = campaigns.stream()
                    .map(campaign -> CompletableFuture.supplyAsync(() -> {
                        float[] campaignEmbedding = campaign.getEmbedding();
                        double similarity = cosineSimilarity(queryEmbedding, campaignEmbedding);
                        System.out.println(similarity);
                        return new SimilarityResult(campaign, similarity);
                    }))
                    .toList();

            // Wait for all futures to complete and collect the results
            List<SimilarityResult> similarityResults = similarityFutures.stream()
                    .map(CompletableFuture::join)
                    .toList();

            // Filter and sort the results based on similarity
            return similarityResults.stream()
                    .filter(result -> result.getSimilarity() > SIMILARITY_THRESHOLD.doubleValue())
                    .sorted((a, b) -> Double.compare(b.getSimilarity(), a.getSimilarity()))
                    .map(SimilarityResult::getCampaign)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Vector search failed", e);
        }
    }

    public List<Campaign> findByFeature(CampaignFeature feature) {
        return repository.findByFeature(feature);
    }


}
