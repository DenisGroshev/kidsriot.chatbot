package com.gmail.denigroshev.kidsriot.chatbot.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatIdExtractor {
    private static final Pattern CHAT_ID_PATTERN = Pattern.compile("\uD83C\uDD94 Chat ID: (\\d+)");

    public static Long extract(String text) {
        if(text == null) return null;
        Matcher m = CHAT_ID_PATTERN.matcher(text);
        if(m.find()) {
            try {
                return Long.parseLong(m.group(1));
            } catch (NumberFormatException ignored) {}
        }
        return null;
    }
}
