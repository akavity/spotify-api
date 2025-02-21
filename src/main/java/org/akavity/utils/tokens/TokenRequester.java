package org.akavity.utils.tokens;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public abstract class TokenRequester {
    protected JsonObject requestToken(String data) {
        HttpURLConnection http = null;
        try {
            URL url = new URL(Endpoints.TOKEN);
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

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
                return JsonParser.parseString(response.toString()).getAsJsonObject();
            }
        } catch (IOException e) {
            System.err.println("Error while executing HTTP request: " + e.getMessage());
            return null;
        } finally {
            if (http != null) {
                http.disconnect();
            }
        }
    }
}
