package org.akavity.utils;

import io.restassured.path.json.JsonPath;
import org.akavity.enums.PathEnum;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public List<String> extractTracks(JsonPath jsonPath, PathEnum pathEnum) {
        return jsonPath.getList(pathEnum.getPath(), String.class)
                .stream()
                .map(x -> extractText(x, "^[^(-]+").toLowerCase())
                .toList();
    }

    public String extractCodeFromURL(String url) {
        return extractText(url, "[?&]code=([^&]+)")
                .replace("?code=", "");
    }

    private String extractText(String text, String regex) {
        String result = "";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            result = (matcher.group().trim());  // matcher.group(1) extractCodeFromURL
        }
        return result;
    }
}
