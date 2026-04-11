package ru.nsu.ermakov;

import java.util.List;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.util.Objects;

public class MarkDownList implements Element {
    private final boolean ordered;
    private final List<Element> items;

    /**
     * Конструктор для MarkDown использующий Builder.
     */
    private MarkDownList (Builder builder) {
        this.ordered = builder.ordered;
        this.items = builder.items;
    }

    /**
     * Преобразование списка в MakrDown.
     */
    @Override
    public String toMarkDown() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < items.size(); i++) {
            Element item = items.get(i);
            String prefix;
            if(ordered) {
                prefix = (i + 1) + ". ";
            } else {
                prefix = "* ";
            }
            sb.append(prefix).append(item.toMarkDown()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Переписанный equals.
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        MarkDownList other = (MarkDownList) obj;
        return ordered == other.ordered && Objects.equals(items, other.items);
    }

    /**
     * Переписанный hashCode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(ordered, items);
    }

    /**
     * Вложенный класс Builder.
     */
    public static class Builder {
        private boolean ordered = false; //считаем что по умолчанию у нас маркированный список
        private List<Element> items = new ArrayList<>();

        /**
         * Изменяем порядок.
         */
        public Builder ordered(boolean isOrdered) {
            this.ordered = isOrdered;
            return this;
        }

        public Builder addItem(Element element) {
            this.items.add(element);
            return this;
        }

        public MarkDownList build() {
            return new MarkDownList(this);
        }
    }
}
