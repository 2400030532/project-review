package com.easyintern.api.dto;

public class AuthDtos {

    private AuthDtos() {
    }

    public static class AuthRequest {
        private String fullName;
        private String pen;
        private String phone;
        private String email;
        private String password;
        private String location;
        private String role;
        private String credential;

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getPen() {
            return pen;
        }

        public void setPen(String pen) {
            this.pen = pen;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getCredential() {
            return credential;
        }

        public void setCredential(String credential) {
            this.credential = credential;
        }
    }

    public static class OtpVerificationRequest {
        private String challengeToken;
        private String otp;

        public String getChallengeToken() {
            return challengeToken;
        }

        public void setChallengeToken(String challengeToken) {
            this.challengeToken = challengeToken;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }
    }

    public static class AuthResponse {
        private Long id;
        private String fullName;
        private String email;
        private String role;
        private String message;
        private boolean otpRequired;
        private String challengeToken;

        public AuthResponse() {
        }

        public AuthResponse(Long id, String fullName, String email, String role, String message) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
            this.role = role;
            this.message = message;
            this.otpRequired = false;
        }

        public AuthResponse(Long id, String fullName, String email, String role, String message, boolean otpRequired, String challengeToken) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
            this.role = role;
            this.message = message;
            this.otpRequired = otpRequired;
            this.challengeToken = challengeToken;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isOtpRequired() {
            return otpRequired;
        }

        public void setOtpRequired(boolean otpRequired) {
            this.otpRequired = otpRequired;
        }

        public String getChallengeToken() {
            return challengeToken;
        }

        public void setChallengeToken(String challengeToken) {
            this.challengeToken = challengeToken;
        }
    }
}
