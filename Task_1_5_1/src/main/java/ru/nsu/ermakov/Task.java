package ru.nsu.ermakov;

import java.util.Objects;

public class Task implements Element {
    private final boolean status;
    private final Element value;

    private Task (Builder builder){
        this.status = builder.status;
        this.value = builder.value;
    }
    /**
     * Максим Вылегжанин лох.
     */
    public String toMarkDown() {
        String statusSymbol = this.status ? "x" : " ";
        return "[" + statusSymbol + "] " + this.value.toMarkDown() + "\n";
    }
    /**
     * Equals.
     */
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj == null || obj.getClass() != getClass()) return false;
        Task other = (Task) obj;
        return status == other.status && value.equals(other.value);
    }
    /**
     * HashCode.
     */
    public int hashCode() {
        return Objects.hash(status, value);
    }
    /**
     * Builder.
     */
    public static class Builder {
        private boolean status = false;
        private Element value;

        public Builder status (boolean status) {
            this.status = status;
            return this;
        }
        public Builder value (Element element) {
            if(element == null) {
                throw new IllegalStateException("Task must have name");
            }
            this.value = element;
            return this;
        }

        public Task build () {
            if(this.value == null) {
                throw new IllegalStateException("Task must have name");
            }
            return new Task(this);
        }
    }
}
