package com.easyintern.api.service;

import com.easyintern.api.dto.AuthDtos.AuthResponse;
import com.easyintern.api.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginOtpService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final JavaMailSender mailSender;
    private final Map<String, OtpChallenge> challenges = new ConcurrentHashMap<>();

    @Value("${app.auth.otp-valid-minutes:10}")
    private long otpValidMinutes;

    @Value("${spring.mail.username:}")
    private String fromAddress;

    @Value("${app.mail.from:no-reply@easyintern.local}")
    private String configuredFromAddress;

    public LoginOtpService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public AuthResponse createChallenge(User user, String message) {
        cleanupExpiredChallenges();

        String challengeToken = UUID.randomUUID().toString();
        String otp = String.format("%06d", RANDOM.nextInt(1_000_000));
        OtpChallenge challenge = new OtpChallenge(
                challengeToken,
                user.getId(),
                user.getEmail(),
                hashOtp(otp),
                Instant.now().plus(Duration.ofMinutes(Math.max(1, otpValidMinutes)))
        );
        challenges.put(challengeToken, challenge);
        sendOtpEmail(user, otp);

        return new AuthResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole(), message, true, challengeToken);
    }

    public OtpChallenge verifyChallenge(String challengeToken, String otp) {
        cleanupExpiredChallenges();
        if (challengeToken == null || challengeToken.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP challenge token is required");
        }
        if (otp == null || otp.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP code is required");
        }

        OtpChallenge challenge = challenges.get(challengeToken);
        if (challenge == null || challenge.isExpired()) {
            challenges.remove(challengeToken);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "OTP challenge expired. Please log in again.");
        }
        if (!MessageDigest.isEqual(challenge.getOtpHash(), hashOtp(otp))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid OTP code");
        }

        challenges.remove(challengeToken);
        return challenge;
    }

    private void sendOtpEmail(User user, String otp) {
        String senderAddress = configuredFromAddress;
        if (senderAddress == null || senderAddress.isBlank()) {
            senderAddress = fromAddress;
        }
        if (senderAddress == null || senderAddress.isBlank()) {
            senderAddress = "no-reply@easyintern.local";
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderAddress);
        message.setTo(user.getEmail());
        message.setSubject("Your EasyIntern login OTP");
        message.setText("Hello " + user.getFullName() + ",\n\nYour EasyIntern login OTP is: " + otp + "\n\nThis OTP expires in " + otpValidMinutes + " minutes. If you did not request this, ignore this email.");
        mailSender.send(message);
    }

    private void cleanupExpiredChallenges() {
        Instant now = Instant.now();
        challenges.entrySet().removeIf(entry -> entry.getValue().getExpiresAt().isBefore(now));
    }

    private byte[] hashOtp(String otp) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(otp.getBytes());
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Unable to hash OTP", ex);
        }
    }

    public static class OtpChallenge {
        private final String challengeToken;
        private final Long userId;
        private final String email;
        private final byte[] otpHash;
        private final Instant expiresAt;

        public OtpChallenge(String challengeToken, Long userId, String email, byte[] otpHash, Instant expiresAt) {
            this.challengeToken = challengeToken;
            this.userId = userId;
            this.email = email;
            this.otpHash = otpHash;
            this.expiresAt = expiresAt;
        }

        public String getChallengeToken() {
            return challengeToken;
        }

        public Long getUserId() {
            return userId;
        }

        public String getEmail() {
            return email;
        }

        public byte[] getOtpHash() {
            return otpHash;
        }

        public Instant getExpiresAt() {
            return expiresAt;
        }

        public boolean isExpired() {
            return expiresAt.isBefore(Instant.now());
        }
    }
}