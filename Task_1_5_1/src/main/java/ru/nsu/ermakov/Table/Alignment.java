package ru.nsu.ermakov.Table;

public enum Alignment {
    LEFT(":-"),
    CENTER(":-:"),
    RIGHT("-:"),
    NONE("---");
    private final String markdownCode;

    Alignment(String markdownCode) {
        this.markdownCode = markdownCode;
    }
    public String getMarkdownCode() {
        return markdownCode;
    }
}
