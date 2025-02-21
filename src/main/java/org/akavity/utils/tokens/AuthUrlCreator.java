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
    private final String POSSIBLE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final SecureRandom RANDOM;
    private final String SCOPE;
    private final String CODE_VERIFIER;
    private final String AUTH_URL;

    public AuthUrlCreator(String scope) {
        this.SCOPE = scope;
        RANDOM = new SecureRandom();
        CODE_VERIFIER = generateRandomString(64);
        byte[] hashed = sha256(CODE_VERIFIER);
        String codeChallenge = base64UrlEncode(hashed);
        AUTH_URL = buildAuthUrl(codeChallenge);
    }

    // Code Verifier
    private String generateRandomString(int RANDOM_LENGTH) {
        StringBuilder sb = new StringBuilder(RANDOM_LENGTH);
        for (int i = 0; i < RANDOM_LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(POSSIBLE_CHARACTERS.length());
            sb.append(POSSIBLE_CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    // Code Challenge
    private byte[] sha256(String plain) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return digest.digest(plain.getBytes(StandardCharsets.UTF_8));
    }

    private String base64UrlEncode(byte[] input) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(input);
    }

    // Request User Authorization
    private String buildAuthUrl(String codeChallenge) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("response_type", "code");
        params.put("client_id", CLIENT_ID);
        params.put("scope", SCOPE);
        params.put("code_challenge_method", "S256");
        params.put("code_challenge", codeChallenge);
        params.put("redirect_uri", REDIRECT_URI);

        String queryParams = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        return "https://accounts.spotify.com/authorize?" + queryParams;
    }

    public String getAUTH_URL() {
        return AUTH_URL;
    }

    public String getCODE_VERIFIER() {
        return CODE_VERIFIER;
    }
}
