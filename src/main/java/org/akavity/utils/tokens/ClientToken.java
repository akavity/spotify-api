package org.akavity.utils.tokens;

import com.google.gson.JsonObject;

public class ClientToken extends TokenRequester {
    private String accessToken = "";

    public ClientToken() {
        getToken();
    }

    private void getToken() {
        String params = "grant_type=client_credentials" +
                "&client_id=" + Endpoints.CLIENT_ID +
                "&client_secret=" + Endpoints.CLIENT_SECRET;

        // Parsing JSON
        JsonObject jsonResponse = requestToken(params);
        if (jsonResponse != null) {
            accessToken = jsonResponse.getAsJsonPrimitive("access_token").getAsString();
        }
    }

    public String getAccessToken() {
        return accessToken;
    }
}
