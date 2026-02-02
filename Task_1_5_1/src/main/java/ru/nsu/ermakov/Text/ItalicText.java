package ru.nsu.ermakov.Text;

import ru.nsu.ermakov.Element;

/**
 * Класс для курсивного текста в формат MarkDown
 */
public class ItalicText implements Element {
    private final String value;

    /**
     * Конструктор класса.
     */
    public ItalicText(String value){
        this.value = value;
    }
    /**
     * Преобразование курсивного текста в строку в формате MakrDown.
     */
    @Override
    public String toMarkDown() {
        return "*" + value + "*\n";
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
        ItalicText other = (ItalicText) obj;
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
