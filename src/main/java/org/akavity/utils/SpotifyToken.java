package org.akavity.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SpotifyToken {
    public String accessToken = "";
    public String expiresIn = "";

    public void get() throws IOException {
        URL url = new URL(Endpoints.TOKEN);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();

        try {
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String data = "grant_type=client_credentials&client_id=" + Endpoints.CLIENT_ID + "&client_secret=" + Endpoints.CLIENT_SECRET;
            byte[] out = data.getBytes(StandardCharsets.UTF_8);

            try (OutputStream stream = http.getOutputStream()) {
                stream.write(out);
            }

            // Проверяем HTTP-код ответа
            int statusCode = http.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Ошибка HTTP-запроса: " + statusCode);
            }

            // Читаем ответ
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // Разбираем JSON
                JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                this.accessToken = jsonResponse.getAsJsonPrimitive("access_token").getAsString();
                this.expiresIn = jsonResponse.getAsJsonPrimitive("expires_in").getAsString();
            }
        } finally {
            http.disconnect();
        }
    }
}
