package ru.nsu.ermakov;
/**
 * Представляет собой элемент Markdown. Этот интерфейс является базовым контрактом, для всех компонентов, которые могут быть сериализованы в строку Markdown
 */
public interface Element {
    /**
     * Преобразование чего-то в строку в формате MakrDown.
     */
     String toMarkDown();

    /**
     * Для всех эллементов наследуемых от этого интерфейса нужно переписать equals.
     */
    @Override
    boolean equals(Object obj);
    /**
     * Для всех эллементов наследуемых от этого интерфейса нужно переписать HashCode.
     */
    @Override
    int hashCode();
}
