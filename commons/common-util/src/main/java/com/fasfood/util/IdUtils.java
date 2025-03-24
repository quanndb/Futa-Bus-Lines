package com.fasfood.util;

import java.util.UUID;
import java.util.regex.Pattern;

public final class IdUtils {
    private static final Pattern UUID_REGEX = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    private IdUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static UUID nextId() {
        return UUID.randomUUID();
    }

    public static boolean checkingIdIsUUID(String id) {
        return !StrUtils.isBlank(id) && UUID_REGEX.matcher(id).matches();
    }

    public static UUID convertStringToUUID(String id) {
        return checkingIdIsUUID(id) ? UUID.fromString(id) : null;
    }
}
