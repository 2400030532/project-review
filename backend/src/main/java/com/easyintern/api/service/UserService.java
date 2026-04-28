package com.easyintern.api.service;

import com.easyintern.api.dto.AuthDtos.AuthRequest;
import com.easyintern.api.dto.AuthDtos.AuthResponse;
import com.easyintern.api.dto.AuthDtos.OtpVerificationRequest;
import com.easyintern.api.model.User;
import com.easyintern.api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.http.HttpStatus;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GoogleAuthService googleAuthService;
    private final LoginOtpService loginOtpService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, GoogleAuthService googleAuthService, LoginOtpService loginOtpService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.googleAuthService = googleAuthService;
        this.loginOtpService = loginOtpService;
    }

    public User registerUser(AuthRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already registered");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setPen(request.getPen());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setLocation(request.getLocation());
        user.setRole(request.getRole() == null ? "student" : request.getRole().toLowerCase());

        return userRepository.save(user);
    }

    public AuthResponse authenticateAndSendOtp(String email, String password, String role) {
        String normalizedRole = role == null || role.isBlank() ? "student" : role.toLowerCase();
        User user = userRepository.findByEmailAndRole(email.toLowerCase(), normalizedRole)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        return loginOtpService.createChallenge(user, "OTP sent to your email address.");
    }

    public AuthResponse authenticateWithGoogle(String credential, String role) {
        GoogleAuthService.GoogleProfile profile = googleAuthService.verifyCredential(credential);
        User user = userRepository.findByEmail(profile.getEmail())
                .orElseGet(() -> createGoogleUser(profile, role));

        if (user.getFullName() == null || user.getFullName().isBlank()) {
            user.setFullName(profile.getDisplayName());
            user = userRepository.save(user);
        }

        return loginOtpService.createChallenge(user, "Google sign-in succeeded. OTP sent to your email address.");
    }

    public AuthResponse verifyOtp(OtpVerificationRequest request) {
        LoginOtpService.OtpChallenge challenge = loginOtpService.verifyChallenge(request.getChallengeToken(), request.getOtp());
        User user = userRepository.findById(challenge.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for OTP challenge"));
        return new AuthResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole(), "Login verified successfully");
    }

    private User createGoogleUser(GoogleAuthService.GoogleProfile profile, String role) {
        User user = new User();
        user.setFullName(profile.getDisplayName());
        user.setEmail(profile.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setRole(role == null || role.isBlank() ? "student" : role.toLowerCase());
        return userRepository.save(user);
    }
}
