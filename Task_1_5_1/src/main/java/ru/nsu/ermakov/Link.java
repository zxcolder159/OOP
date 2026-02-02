package ru.nsu.ermakov;

import java.util.Objects;

public class Link implements Element {
    private final Element value;
    private final String url;

    /**
     * Конструктор
     */
    private Link (Builder builder) {
        this.value = builder.value;
        this.url = builder.url;
    }
    /**
     * MakrDown.
     */
    @Override
    public String toMarkDown() {
        return "[" + value.toMarkDown() + "](" + url + ")\n";
    }
    /**
     * Equals.
     */
    @Override
    public boolean equals (Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Link other = (Link) obj;
        return url.equals(other.url) && value.equals(other.value);
    }
    /**
     * HashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(value, url);
    }
    /**
     * Builder.
     */
    public static class Builder {
        private Element value;
        private String url;

        public Builder value (Element element){
            if(element == null) {
                throw new IllegalStateException("Link must have name.");
            }
            this.value = element;
            return this;
        }

        public Builder url(String url){
            this.url = url;
            return this;
        }
        public Link build () {
            if (this.value == null) {
                throw new IllegalStateException("Link must have a value (display text).");
            }
            if (this.url == null || this.url.trim().isEmpty()) {
                throw new IllegalStateException("Link must have a URL.");
            }
            return new Link(this);
        }
    }
}
