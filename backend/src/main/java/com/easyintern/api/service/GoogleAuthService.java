package com.easyintern.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
public class GoogleAuthService {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper;

    @Value("${app.google.client-id:}")
    private String clientId;

    public GoogleAuthService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public GoogleProfile verifyCredential(String credential) {
        if (credential == null || credential.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Google credential is required");
        }
        if (clientId == null || clientId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Google client ID is not configured");
        }

        Map<String, Object> claims = fetchTokenClaims(credential);
        String audience = stringValue(claims.get("aud"));
        if (!clientId.equals(audience)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Google token audience mismatch");
        }

        String email = stringValue(claims.get("email"));
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Google account email is missing");
        }

        Object emailVerifiedValue = claims.get("email_verified");
        if (emailVerifiedValue == null) {
            emailVerifiedValue = claims.get("verified_email");
        }
        if (!Boolean.parseBoolean(String.valueOf(emailVerifiedValue))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Google account email is not verified");
        }

        Map<String, Object> payload = decodeJwtPayload(credential);
        String displayName = stringValue(payload.get("name"));
        if (displayName == null || displayName.isBlank()) {
            displayName = email.substring(0, email.indexOf('@'));
        }

        return new GoogleProfile(displayName, email, stringValue(payload.get("sub")));
    }

    private Map<String, Object> fetchTokenClaims(String credential) {
        try {
            String encodedCredential = URLEncoder.encode(credential, StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://oauth2.googleapis.com/tokeninfo?id_token=" + encodedCredential))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Google token verification failed");
            }
            return objectMapper.readValue(response.body(), new TypeReference<Map<String, Object>>() {});
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Unable to verify Google token", ex);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Google token verification interrupted", ex);
        }
    }

    private Map<String, Object> decodeJwtPayload(String credential) {
        try {
            String[] parts = credential.split("\\.");
            if (parts.length < 2) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Google credential format");
            }
            byte[] decoded = Base64.getUrlDecoder().decode(parts[1]);
            return objectMapper.readValue(decoded, new TypeReference<Map<String, Object>>() {});
        } catch (IllegalArgumentException | IOException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unable to decode Google token payload", ex);
        }
    }

    private String stringValue(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    public static class GoogleProfile {
        private final String displayName;
        private final String email;
        private final String subject;

        public GoogleProfile(String displayName, String email, String subject) {
            this.displayName = displayName;
            this.email = email;
            this.subject = subject;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getEmail() {
            return email;
        }

        public String getSubject() {
            return subject;
        }
    }
}