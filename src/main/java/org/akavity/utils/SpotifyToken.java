package org.akavity.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.http.Header;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SpotifyToken {
    private String accessToken = "";
    private String expiresIn = "";

    public SpotifyToken() {
        get();
    }

    private void get() {
        HttpURLConnection http = null;
        try {
            URL url = new URL(Endpoints.TOKEN);
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String data = "grant_type=client_credentials&client_id=" + Endpoints.CLIENT_ID + "&client_secret=" + Endpoints.CLIENT_SECRET;
            byte[] out = data.getBytes(StandardCharsets.UTF_8);

            try (OutputStream stream = http.getOutputStream()) {
                stream.write(out);
            }

            // Checking the HTTP response code
            int statusCode = http.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("HTTP request error: " + statusCode);
            }

            // Read the answer
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // Parsing JSON Разбираем JSON
                JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                accessToken = jsonResponse.getAsJsonPrimitive("access_token").getAsString();
                expiresIn = jsonResponse.getAsJsonPrimitive("expires_in").getAsString();
            }
        } catch (IOException e) {
            System.err.println("Error while executing HTTP request: " + e.getMessage());
        } finally {
            if (http != null) {
                http.disconnect();
            }
        }
    }

    public Header getAuthHeader() {
        return new Header("Authorization", "Bearer " + getAccessToken());
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }
}
