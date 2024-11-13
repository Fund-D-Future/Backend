package com.funddfuture.fund_d_future.payment;

import com.funddfuture.fund_d_future.donation.DonationRequest;
import com.funddfuture.fund_d_future.donation.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import okhttp3.*;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;
    private final DonationService donationService;
    private final OkHttpClient client = new OkHttpClient().newBuilder().build();

    public String processCardPayment(PaymentRequest request) throws Exception {

        // Set media type for JSON payload
        MediaType mediaType = MediaType.parse("application/json");

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
                request.getTransactionReference(),
                request.getCurrency(),
                request.getDescription(),
                request.getCard().getExpiryMonth(),
                request.getCard().getExpiryYear(),
                request.getCard().getSecurityCode(),
                request.getCard().getCardNumber(),
                request.getCallbackUrl()
        );

        // Set the JSON payload as the request body
        RequestBody body = RequestBody.create(jsonPayload, mediaType);

        String apiKey = request.getToken();
        String encodedApiKey = Base64.getEncoder().encodeToString(apiKey.getBytes());
        System.out.println(encodedApiKey);

        // Build the HTTP request
        Request httpRequest = new Request.Builder()
                .url("https://cards-live.78financials.com/card_charge/")
                .post(body)
                .addHeader("Authorization", "Payaza " + encodedApiKey)
                .addHeader("Content-Type", "application/json")
                .addHeader("X-TenantID", "test")
                .build();

        // Execute the request and return the response
        try (Response response = client.newCall(httpRequest).execute()) {
            if (response.body() != null) {
                return response.body().string();
            } else {
                throw new IOException("Empty response body");
            }
        }
    }

    public void processPayment(PaymentRequest request) {
        var payment = Payment.builder()
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .build();
        repository.save(payment);

        // Create and save donation
        var donationRequest = DonationRequest.builder()
                .id(UUID.randomUUID())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .build();
        donationService.save(donationRequest);

        // Update total payments in campaign
        // donationService.updateRaisedFunding(request.getCampaign().getId());
    }

    public List<Payment> findByDonorId(UUID donorId) {
        return repository.findByDonorId(donorId);
    }

    public List<Payment> findByCampaignId(UUID campaignId) {
        return repository.findByCampaignId(campaignId);
    }
}
