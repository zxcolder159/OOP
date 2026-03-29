package ru.nsu.ermakov;

import java.lang.StringBuilder;
import java.util.Objects;

public class CodeBlock implements Element {
    private final String language;
    private final String value;

    private CodeBlock(Builder builder) {
        this.language = builder.language;
        this.value = builder.value;
    }

    @Override
    public String toMarkDown() {
        StringBuilder sb = new StringBuilder();
        sb.append("```");
        if(language != null && !language.trim().isEmpty()) {
            sb.append(language.trim());
        }
        sb.append("\n");
        sb.append(value);
        if(!value.endsWith("\n")) {
            sb.append("\n");
        }
        sb.append("```\n");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        CodeBlock other = (CodeBlock) obj;
        return Objects.equals(this.value, other.value) && Objects.equals(this.language, other.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(language, value);
    }

    public static class Builder {
        private String language;
        private String value;
        public Builder language(String string) {
            this.language = string;
            return this;
        }
        public Builder value (String string) {
            this.value = string;
            return this;
        }
        public CodeBlock build() {
            if(value == null) {
                throw new IllegalStateException("Block must have text");
            }
            return new CodeBlock(this);
        }
    }
}
