package com.funddfuture.fund_d_future.payment;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    private final ConcurrentHashMap<String, String> otpCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> otpExpiryCache = new ConcurrentHashMap<>();
    private static final long OTP_EXPIRY_DURATION = TimeUnit.MINUTES.toMillis(10); // 10 minutes

    public void saveOtp(String email, String otp) {
        otpCache.put(email, otp);
        otpExpiryCache.put(email, System.currentTimeMillis() + OTP_EXPIRY_DURATION);
    }

    public boolean validateOtp(String email, String otp) {
        if (!otpCache.containsKey(email)) {
            return false;
        }
        if (System.currentTimeMillis() > otpExpiryCache.get(email)) {
            otpCache.remove(email);
            otpExpiryCache.remove(email);
            return false;
        }
        return otp.equals(otpCache.get(email));
    }
}
