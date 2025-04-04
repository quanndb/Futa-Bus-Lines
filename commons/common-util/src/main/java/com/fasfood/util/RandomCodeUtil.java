package com.fasfood.util;

import java.security.SecureRandom;
import java.time.Instant;

public class RandomCodeUtil {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int RANDOM_LENGTH = 3;

    public static String generateOrderCode(String prefix) {
        String timePart = Long.toString(Instant.now().toEpochMilli(), 36).toUpperCase();

        StringBuilder randomPart = new StringBuilder();
        for (int i = 0; i < RANDOM_LENGTH; i++) {
            randomPart.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return String.format("%s%s%s", prefix, timePart, randomPart);
    }
}
