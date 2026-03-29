package ru.nsu.ermakov;

import java.lang.StringBuilder;
import java.util.Objects;

public class Heading implements Element {
    private final int lvl;
    private final Element value;

    /**
     * Конструктор.
     */
    private Heading(Builder builder) {
        this.lvl = builder.lvl;
        this.value = builder.value;
    }

    /**
     * Преобразование заголовков в MakrDown.
     */
    @Override
    public String toMarkDown() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < lvl; i++) {
            sb.append("#");
        }
        sb.append(" ");

        sb.append(this.value.toMarkDown());
        sb.append("\n");
        return sb.toString();
    }
    /**
     * Переписанный equals.
     */
    @Override
    public boolean equals (Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Heading other = (Heading) obj;
        return lvl == other.lvl && value.equals(other.value);
    }
    /**
     * hashCode
     */
    @Override
    public int hashCode(){
        return Objects.hash(lvl, value);
    }
    /**
     * Вложенный класс Builder.
     */
    public static class Builder {
        private int lvl = 1;
        private Element value = null;
        public Builder lvl(int lvl) {
            this.lvl = lvl;
            return this;
        }
        public Builder value(Element value){
            this.value = value;
            return this;
        }
        public Heading build() {
            if (lvl < 1 || lvl > 6) {
                throw new IllegalArgumentException("Heading level must be between 1 and 6.");
            }
            if (value == null) {
                throw new IllegalStateException("Heading must have content.");
            }
            return new Heading(this);
        }
    }
}
