package org.akavity.utils;

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
        String currentLine = Lines.readLine();
        StringBuilder response = new StringBuilder();
        while (currentLine != null) {
            response.append(currentLine).append("\n");
            currentLine = Lines.readLine();
        }

        this.accessToken = String.valueOf(JsonParser.parseString(String.valueOf(response)).getAsJsonObject().getAsJsonPrimitive("access_token"));
        this.expiresIn = String.valueOf(JsonParser.parseString(String.valueOf(response)).getAsJsonObject().getAsJsonPrimitive("expires_in"));

        http.disconnect();
    }
}
