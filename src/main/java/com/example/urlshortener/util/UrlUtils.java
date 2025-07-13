package com.example.urlshortener.util;


import java.security.SecureRandom;
import java.util.regex.Pattern;

public class UrlUtils {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SHORT_CODE_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    private static final Pattern URL_PATTERN = Pattern.compile(
            "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$",
            Pattern.CASE_INSENSITIVE
    );

    public static String generateShortCode() {
        StringBuilder sb = new StringBuilder(SHORT_CODE_LENGTH);
        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public static boolean isValidUrl(String url) {
        return url != null && URL_PATTERN.matcher(url).matches();
    }

    public static String normalizeUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return null;
        }

        url = url.trim();

        // Add protocol if missing
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }

        return url;
    }
}
