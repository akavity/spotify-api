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
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("content-type", "application/x-www-form-urlencoded");

        String data = "grant_type=client_credentials&client_id=" + Endpoints.CLIENT_ID + "&client_secret=" + Endpoints.CLIENT_SECRET + "";

        byte[] out = data.getBytes(StandardCharsets.UTF_8);

        OutputStream stream = http.getOutputStream();
        stream.write(out);

        BufferedReader Lines = new BufferedReader(new InputStreamReader(http.getInputStream()));
        StringBuilder response = new StringBuilder();
        String currentLine;
        while ((currentLine = Lines.readLine()) != null) {
            response.append(currentLine);
        }

        JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
        this.accessToken = jsonResponse.getAsJsonPrimitive("access_token").getAsString();
        this.expiresIn = jsonResponse.getAsJsonPrimitive("expires_in").getAsString();

        http.disconnect();
    }
}
