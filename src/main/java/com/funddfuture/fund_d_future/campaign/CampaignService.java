package com.funddfuture.fund_d_future.campaign;

import com.funddfuture.fund_d_future.file.File;
import com.funddfuture.fund_d_future.file.FileRepository;
import com.funddfuture.fund_d_future.file.FileService;
import com.funddfuture.fund_d_future.user.User;
import com.funddfuture.fund_d_future.user.UserRepository;
import com.funddfuture.fund_d_future.wallet.Wallet;
import com.funddfuture.fund_d_future.wallet.WalletRepository;
import com.funddfuture.fund_d_future.wallet.WalletService;
import com.funddfuture.fund_d_future.wallet.WithdrawalRequest;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import static org.springframework.ai.vectorstore.SimpleVectorStore.EmbeddingMath.cosineSimilarity;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final WalletRepository walletRepository;
    private static final Number SIMILARITY_THRESHOLD = 0.4;
    private final CampaignRepository repository;
    private final OpenAiEmbeddingModel aiClient;
    private final FileService fileService;
    private final WalletService walletService;
    private final PasswordEncoder passwordEncoder;
    private final OkHttpClient client = new OkHttpClient().newBuilder().build();


    @Value("${spring.ai.openai.api-key:#{null}}")
    private String openAiApiKey;

    private final boolean enableAiSearch = openAiApiKey != null && !openAiApiKey.isBlank();

    @Value("${encoded-payaza-api-key}")
    private String encodedPayazaApiKey;

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

        // Create and save wallet for the campaign
        walletService.createWalletForCampaign(campaign.getId());

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

    public List<File> uploadFiles(UUID campaignId, List<MultipartFile> files) throws IOException {
        Campaign campaign = repository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));
        List<File> uploadedFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            File uploadedFile = fileService.uploadPublicFile(file);
            uploadedFiles.add(uploadedFile);
        }

        campaign.getFiles().addAll(uploadedFiles);
        repository.save(campaign);

        return uploadedFiles;
    }

    public void deleteFile(UUID campaignId, UUID fileId) {
        Campaign campaign = repository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        if (!campaign.getFiles().contains(file)) {
            throw new RuntimeException("File does not belong to the campaign");
        }

        fileService.deletePublicFile(fileId);
        campaign.getFiles().remove(file);
        repository.save(campaign);
    }

    public ResponseEntity<Object> withdrawFunds(UUID campaignId, WithdrawalRequest request, UUID userId) throws IOException {
        Campaign campaign = repository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        if (!campaign.getOwner().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        Wallet wallet = walletRepository.findByCampaignId(campaignId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (wallet.getBalance() < request.getAmount()) {
            throw new RuntimeException("Insufficient funds");
        }
        

        CharSequence tpin = request.getTransactionPin();
        boolean passwordMatches = passwordEncoder.matches(tpin, campaign.getOwner().getTransactionPin());
        System.out.println(tpin);
        System.out.println(passwordMatches);

        if (!passwordMatches) {
            throw new RuntimeException("Invalid transaction pin");
        }

        // Deduct the amount from the wallet balance
        wallet.setBalance(wallet.getBalance() - request.getAmount());
        walletRepository.save(wallet);

        // Prepare the JSON payload
        String jsonPayload = String.format(
                "{" +
                        "\"transaction_type\": \"nuban\"," +
                        "\"service_payload\": {" +
                        "\"payout_amount\": %.2f," +
                        "\"transaction_pin\": \"%s\"," +
                        "\"account_reference\": \"%s\"," +
                        "\"currency\": \"NGN\"," +
                        "\"country\": \"NGA\"," +
                        "\"payout_beneficiaries\": [" +
                        "{" +
                        "\"credit_amount\": %.2f," +
                        "\"account_number\": \"%s\"," +
                        "\"account_name\": \"%s\"," +
                        "\"bank_code\": \"%s\"," +
                        "\"narration\": \"Campaign Withdrawal\"," +
                        "\"transaction_reference\": \"%s\"," +
                        "\"sender\": {" +
                        "\"sender_name\": \"Fund D Future\"," +
                        "\"sender_id\": \"\"," +
                        "\"sender_phone_number\": \"01234595\"," +
                        "\"sender_address\": \"123, Ace Street\"" +
                        "}" +
                        "}" +
                        "]" +
                        "}" +
                        "}",
                request.getAmount(),
                request.getTransactionPin(),
                campaignId,
                request.getAmount(),
                request.getAccountNumber(),
                request.getAccountName(),
                request.getBankCode(),
                UUID.randomUUID()
        );

        // Set the JSON payload as the request body
        RequestBody body = RequestBody.create(jsonPayload, MediaType.parse("application/json"));

        // Build the HTTP request
        Request httpRequest = new Request.Builder()
                .url("https://api.payaza.africa/live/payout-receptor/payout")
                .post(body)
                .addHeader("Authorization", "Payaza " + encodedPayazaApiKey)
                .addHeader("X-tenantID", "test")
                .addHeader("Content-Type", "application/json")
                .build();

        return ResponseEntity.ok(client.newCall(httpRequest).execute());
    }


}
