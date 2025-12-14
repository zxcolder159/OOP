package ru.nsu.ermakov;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Цитата.
 */
public class Blockquote implements Element {
    private final List<Element> items;

    private Blockquote (Builder builder) {
        this.items = builder.items;
    }
    /**
     * Преобразание цитаты в MarkDown.
     */
    @Override
    public String toMarkDown() {
        StringBuilder sb = new StringBuilder();
        for (Element item : items) {
            String itemMarkDown = item.toMarkDown();
            String[] lines = itemMarkDown.split("\r?\n", -1);
            for (String line : lines) {
                sb.append("> ").append(line).append("\n");
            }
        }

        return sb.toString();

    }
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Blockquote other = (Blockquote) obj;
        return Objects.equals(items, other.items);
    }

    /**
     * Переписанный hashCode.
     */
    @Override
    public int hashCode() {
        return items.hashCode();
    }
    /**
     * Вложенный класс Builder.
     */
    public static class Builder {
        private List<Element> items = new ArrayList<>();

        public Blockquote.Builder addItem(Element element) {
            this.items.add(element);
            return this;
        }
        public Blockquote build () {
            return new Blockquote(this);
        }
    }
}
