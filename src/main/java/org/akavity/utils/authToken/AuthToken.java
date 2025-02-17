package org.akavity.utils.authToken;

import com.google.gson.JsonObject;
import org.akavity.utils.Endpoints;
import org.akavity.utils.TokenRequester;

public class AuthToken extends TokenRequester {
    private String accessToken = "";
    private String refreshToken = "";

    public AuthToken(String code, String codeVerifier) {
        getToken(code, codeVerifier);
    }

    private void getToken(String code, String codeVerifier) {
        String params = "client_id=" + Endpoints.CLIENT_ID +
                "&grant_type=authorization_code" +
                "&code=" + code +
                "&redirect_uri=" + Endpoints.REDIRECT_URI +
                "&code_verifier=" + codeVerifier;

        JsonObject jsonResponse = requestToken(params);
        if (jsonResponse != null) {
            accessToken = jsonResponse.getAsJsonPrimitive("access_token").getAsString();
            refreshToken = jsonResponse.getAsJsonPrimitive("refresh_token").getAsString();
        }
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
