package org.akavity.utils.tokens;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.akavity.utils.tokens.Endpoints.CLIENT_ID;
import static org.akavity.utils.tokens.Endpoints.REDIRECT_URI;

public class AuthUrlCreator {
    private static final String POSSIBLE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_VERIFIER_LENGTH = 64;
    private final SecureRandom random;
    private final String scope;
    private final String codeVerifier;
    private final String authUrl;

    public AuthUrlCreator(String scope) {
        this.scope = scope;
        this.random = new SecureRandom();
        this.codeVerifier = generateRandomString(CODE_VERIFIER_LENGTH);
        byte[] hashed = sha256(codeVerifier);
        String codeChallenge = base64UrlEncode(hashed);
        this.authUrl = buildAuthUrl(codeChallenge);
    }

    // Code Verifier
    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(POSSIBLE_CHARACTERS.length());
            sb.append(POSSIBLE_CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    // Code Challenge
    private byte[] sha256(String plain) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(plain.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private String base64UrlEncode(byte[] input) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(input);
    }

    // Request User Authorization
    private String buildAuthUrl(String codeChallenge) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("response_type", "code");
        params.put("client_id", CLIENT_ID);
        params.put("scope", scope);
        params.put("code_challenge_method", "S256");
        params.put("code_challenge", codeChallenge);
        params.put("redirect_uri", REDIRECT_URI);

        String queryParams = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        return "https://accounts.spotify.com/authorize?" + queryParams;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public String getCodeVerifier() {
        return codeVerifier;
    }
}
