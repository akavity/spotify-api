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
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String SCOPE = "user-read-private user-read-email";

    // Code Verifier
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(POSSIBLE_CHARACTERS.length());
            sb.append(POSSIBLE_CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    // Code Challenge
    public static byte[] sha256(String plain) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(plain.getBytes(StandardCharsets.UTF_8));
    }

    public static String base64UrlEncode(byte[] input) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(input);
    }

    // Request User Authorization
    public static String buildAuthUrl(String codeChallenge) {
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
}
