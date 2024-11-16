package com.funddfuture.fund_d_future.payment;

import com.funddfuture.fund_d_future.campaign.Campaign;
import com.funddfuture.fund_d_future.campaign.CampaignRepository;
import com.funddfuture.fund_d_future.donation.DonationRequest;
import com.funddfuture.fund_d_future.donation.DonationService;
import com.funddfuture.fund_d_future.user.MailerService;
import com.funddfuture.fund_d_future.user.User;
import com.funddfuture.fund_d_future.user.UserRepository;
import com.funddfuture.fund_d_future.wallet.Wallet;
import com.funddfuture.fund_d_future.wallet.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final PaymentRepository repository;
    private final WalletRepository walletRepository;
    private final DonationService donationService;
    private final MailerService mailerService;
    private final OtpService otpService;

    private final OkHttpClient client = new OkHttpClient().newBuilder().build();

    @Value("${callback-url}")
    private String callbackUrl;

    @Value("${encoded-payaza-api-key}")
    private String encodedPayazaApiKey;

    private String generateRandomTransactionId() {
        Random random = new Random();
        long randomNumber = (long) (random.nextDouble() * 100_000_000_000_000L); // 15-digit max
        return String.format("%014d", randomNumber); // Ensure it's exactly 15 digits
    }

    public String processCardPayment(PaymentRequest request, UUID campaignId) throws Exception {

        if (!campaignRepository.existsById(campaignId)) {
            throw new Exception("Campaign does not exist");
        }
        // Generate and send OTP
        String otp = generateOtp();
        mailerService.sendOtpEmail(request.getEmailAddress(), otp);

        // Save OTP in the database (or cache) for later validation
        otpService.saveOtp(request.getEmailAddress(), otp);

        // Return a response indicating that OTP has been sent
        return "OTP sent to email";
    }


    public String completeCardPayment(PaymentRequest request, String otp, UUID campaignId) throws Exception {

        // Validate OTP
        if (!otpService.validateOtp(request.getEmailAddress(), otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        String transactionId = generateRandomTransactionId();
        System.out.println("Transaction ID: " + transactionId);
        // checks campaign repository if campaign exists, else throw error
        Optional<Campaign> campaign = campaignRepository.findById(campaignId);
        if (!campaignRepository.existsById(campaignId)) {
            throw new Exception("Campaign does not exist");
        }


        // Set media type for JSON payload
        MediaType mediaType = MediaType.parse("application/json");
        Optional<User> donor = userRepository.findByEmail(request.getEmailAddress());

        // Create JSON payload with Java variables
        String jsonPayload = String.format(
                "{" +
                        "\"service_payload\":{" +
                        "\"first_name\":\"%s\"," +
                        "\"last_name\":\"%s\"," +
                        "\"email_address\":\"%s\"," +
                        "\"phone_number\":\"%s\"," +
                        "\"amount\":%.2f," +
                        "\"transaction_reference\":\"%s\"," +
                        "\"currency\":\"%s\"," +
                        "\"description\":\"%s\"," +
                        "\"card\":{" +
                        "\"expiryMonth\":\"%s\"," +
                        "\"expiryYear\":\"%s\"," +
                        "\"securityCode\":\"%s\"," +
                        "\"cardNumber\":\"%s\"" +
                        "}," +
                        "\"callback_url\":\"%s\"" +
                        "}" +
                        "}",
                request.getFirstName(),
                request.getLastName(),
                request.getEmailAddress(),
                request.getPhoneNumber(),
                request.getAmount(),
                "T"+ transactionId,
                request.getCurrency(),
                request.getDescription(),
                request.getCard().getExpiryMonth(),
                request.getCard().getExpiryYear(),
                request.getCard().getSecurityCode(),
                request.getCard().getCardNumber(),
                callbackUrl
        );


        // Set the JSON payload as the request body
        RequestBody body = RequestBody.create(jsonPayload, mediaType);

//        String encodedApiKey = Base64.getEncoder().encodeToString(payazaApiKey.getBytes());
        System.out.println(encodedPayazaApiKey);


        // Build the HTTP request
        Request httpRequest = new Request.Builder()
                .url("https://cards-live.78financials.com/card_charge/")
                .post(body)
                .addHeader("Authorization", "Payaza " + encodedPayazaApiKey)
                .addHeader("Content-Type", "application/json")
                .addHeader("X-TenantID", "test")
                .build();

        // Execute the request and return the response
        try (Response response = client.newCall(httpRequest).execute()) {
            if (response.body() != null) {

                try {
                var payment = Payment.builder()
                        .amount(request.getAmount())
                        .currency(request.getCurrency())
                        .transactionReference("T" + transactionId)
                        // set to null if there is no donor
                        .donor(donor.orElse(null))
                        .campaign(campaign.orElseThrow())
                        .build();
                repository.save(payment);
                } catch (Exception e) {
                    throw new Exception("Error saving payment: " + e.getMessage());
                }

                try {
                // Create and save donation
                var donationRequest = DonationRequest.builder()
                        .id(UUID.randomUUID())
                        .amount(request.getAmount())
                        .currency(request.getCurrency())
                        .campaign(campaign.orElseThrow())
                        .donor(donor.orElse(null))
                        .build();
                donationService.save(donationRequest);
                try{
                    donationService.updateRaisedFunding(campaignId, request.getAmount());
                } catch (Exception e) {
                    throw new Exception("Error saving donation: " + e.getMessage());
                }

                } catch (Exception e) {
                    throw new Exception("Error saving donation: " + e.getMessage());
                }

                // Update wallet balance
                Wallet wallet = walletRepository.findByCampaignId(campaignId)
                        .orElseThrow(() -> new RuntimeException("Wallet not found"));
                wallet.setBalance(wallet.getBalance() + request.getAmount());
                walletRepository.save(wallet);

                return response.body().string();
            } else {
                throw new IOException("Empty response body");
            }
        }
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public List<Payment> findByDonorId(UUID donorId) {
        return repository.findByDonorId(donorId);
    }

    public List<Payment> findByCampaignId(UUID campaignId) {
        return repository.findByCampaignId(campaignId);
    }
}
