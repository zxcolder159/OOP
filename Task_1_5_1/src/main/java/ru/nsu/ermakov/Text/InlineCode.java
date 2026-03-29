package ru.nsu.ermakov.Text;

import ru.nsu.ermakov.Element;
/**
 * Класс для выделения текста в формат MarkDown
 */
public class InlineCode implements Element {
    private final String value;

    /**
     * Конструктор класса.
     */
    public InlineCode(String value){
        this.value = value;
    }
    /**
     * Преобразование выделения текста в строку в формате MakrDown.
     */
    @Override
    public String toMarkDown() {
        return "`" + value + "`\n";
    }

    /**
     * Проверка на равенство, объекты должны быть одного класса и иметь одинаковое поле value.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        InlineCode other = (InlineCode) obj;
        return value.equals(other.value);
    }
    /**
     * hashCode
     */
    @Override
    public int hashCode() {
        return this.value.hashCode();
    }
}
