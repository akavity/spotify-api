package org.akavity.utils;

import java.util.HashMap;
import java.util.Map;

public class SpotifyHeaders {
    private final Map<String, String> headers;

    public SpotifyHeaders(ClientToken token) {
        this.headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token.getAccessToken());
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
