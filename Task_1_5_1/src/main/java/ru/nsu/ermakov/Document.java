package ru.nsu.ermakov;

import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Document implements Element {
    private final List<Element> elements;

    private Document(Builder builder){
        this.elements = builder.elements;
    }
    @Override
    public String toMarkDown() {
        StringBuilder sb = new StringBuilder();
        for(Element element : elements) {
            sb.append(element.toMarkDown());
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document other = (Document) o;
        return Objects.equals(elements, other.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements);
    }

    public static class Builder {
        private final List<Element> elements = new ArrayList<>();
        public Builder addElement(Element element) {
            if (element != null) {
                this.elements.add(element);
            }
            return this;
        }
        public Document build() {
            return new Document(this);
        }
    }
}
