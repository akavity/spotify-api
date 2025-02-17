package org.akavity.utils.authToken;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.akavity.utils.Endpoints;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.akavity.utils.Endpoints.CLIENT_ID;
import static org.akavity.utils.Endpoints.REDIRECT_URI;

public class AuthToken {
    private String accessToken = "";
    private String refreshToken = "";

    public AuthToken(String code, String codeVerifier) {
        getToken(code, codeVerifier);
    }

    private void getToken(String code, String codeVerifier) {
        HttpURLConnection http = null;
        try {
            URL url = new URL(Endpoints.TOKEN);
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String data = "client_id=" + CLIENT_ID +
                    "&grant_type=authorization_code" +
                    "&code=" + code +
                    "&redirect_uri=" + REDIRECT_URI +
                    "&code_verifier=" + codeVerifier;
            byte[] out = data.getBytes(StandardCharsets.UTF_8);

            try (OutputStream stream = http.getOutputStream()) {
                stream.write(out);
            }

            // Checking the HTTP response code Проверяем HTTP-код ответа
            int statusCode = http.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("HTTP request error Ошибка HTTP-запроса: " + statusCode);
            }

            // Read the answer Читаем ответ
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // Parsing JSON Разбираем JSON
                JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                accessToken = jsonResponse.getAsJsonPrimitive("access_token").getAsString();
                refreshToken = jsonResponse.getAsJsonPrimitive("refresh_token").getAsString();
            }
        } catch (IOException e) {
            System.err.println("Error while executing HTTP request Ошибка при выполнении HTTP-запроса: " + e.getMessage());
            //e.printStackTrace(); // Можно убрать в продакшене или заменить на логгер
        } finally {
            if (http != null) {
                http.disconnect();
            }
        }
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
