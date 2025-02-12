package org.akavity.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public String extractText(String text, String regex) {
        String result = "";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            result = (matcher.group().replace("(", "").trim());
        }
        return result;
    }
}
