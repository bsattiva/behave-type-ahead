package com.behave;

import java.util.regex.Pattern;

public class Helper {

    public static String capitalize(final String value) {
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }
    public static String getStringWithPattern(final String body, final String regex, final int group) {
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(body);
        if (matcher.find()) {
            return matcher.group(group);
        }
        return "";
    }
}
