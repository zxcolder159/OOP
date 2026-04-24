package ru.nsu.ermakov.checker;

public final class OutputUtils {

    private OutputUtils() {
    }

    public static String firstLine(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }
        String[] lines = text.split("\\R", 2);
        return lines[0].trim();
    }

    public static String shorten(String text, int maxLength) {
        if (text == null || text.isBlank()) {
            return "";
        }
        String compact = text.replaceAll("\\s+", " ").trim();
        if (compact.length() <= maxLength) {
            return compact;
        }
        return compact.substring(0, maxLength) + "...";
    }
}
