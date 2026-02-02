package ru.nsu.ermakov.Text;

import org.junit.jupiter.api.Test;
import ru.nsu.ermakov.Element;

import static org.junit.jupiter.api.Assertions.*;

class TextTest {

    // ----------------------------------------------------------------------
    // Тесты метода toMarkDown()
    // ----------------------------------------------------------------------

    /**
     * Проверка корректного преобразования простой строки.
     * Ожидается, что метод просто добавляет перенос строки (\n).
     */
    @Test
    void toMarkDown_shouldReturnTextWithValueAndNewline() {
        String content = "Это простой текст.";
        Text text = new Text(content);
        String expected = content + "\n";

        assertEquals(expected, text.toMarkDown(),
                "toMarkDown должен вернуть исходный текст, за которым следует \\n");
    }

    /**
     * Проверка работы с пустым текстом.
     * Ожидается, что метод вернет только перенос строки (\n).
     */
    @Test
    void toMarkDown_shouldReturnOnlyNewlineForEmptyString() {
        String content = "";
        Text text = new Text(content);
        String expected = "\n";

        assertEquals(expected, text.toMarkDown(),
                "toMarkDown должен вернуть только \\n для пустой строки");
    }

    /**
     * Проверка текста с уже существующим переносом строки.
     * Ожидается, что метод добавит дополнительный перенос строки.
     */
    @Test
    void toMarkDown_shouldAddExtraNewlineIfContentAlreadyEndsWithIt() {
        String content = "Текст с переносом\n";
        Text text = new Text(content);
        String expected = "Текст с переносом\n\n";

        assertEquals(expected, text.toMarkDown(),
                "toMarkDown должен добавить еще один \\n, даже если он уже есть");
    }

    // ----------------------------------------------------------------------
    // Тесты метода equals()
    // ----------------------------------------------------------------------

    /**
     * Проверка равенства объектов с одинаковым содержимым.
     */
    @Test
    void equals_shouldReturnTrueForSameContent() {
        Text text1 = new Text("Hello");
        Text text2 = new Text("Hello");

        assertTrue(text1.equals(text2),
                "Объекты с одинаковым содержимым должны быть равны");
    }

    /**
     * Проверка неравенства объектов с разным содержимым.
     */
    @Test
    void equals_shouldReturnFalseForDifferentContent() {
        Text text1 = new Text("Hello");
        Text text2 = new Text("World");

        assertFalse(text1.equals(text2),
                "Объекты с разным содержимым не должны быть равны");
    }

    /**
     * Проверка равенства объекта самому себе.
     */
    @Test
    void equals_shouldReturnTrueForSameObject() {
        Text text = new Text("Test");
        assertTrue(text.equals(text),
                "Объект должен быть равен самому себе");
    }

    /**
     * Проверка равенства с null.
     */
    @Test
    void equals_shouldReturnFalseForNull() {
        Text text = new Text("Test");
        assertFalse(text.equals(null),
                "Объект не должен быть равен null");
    }

    /**
     * Проверка равенства с объектом другого класса.
     */
    @Test
    void equals_shouldReturnFalseForDifferentClass() {
        Text text = new Text("Test");
        Element other = new Element() { // Анонимный класс для имитации другого Element
            @Override
            public String toMarkDown() { return ""; }
        };
        assertFalse(text.equals(other),
                "Объект не должен быть равен объекту другого класса");
    }

    // ----------------------------------------------------------------------
    // Тесты метода hashCode()
    // ----------------------------------------------------------------------

    /**
     * Проверка, что хеш-коды одинаковых объектов совпадают (Контракт equals-hashCode).
     */
    @Test
    void hashCode_shouldBeEqualForEqualObjects() {
        Text text1 = new Text("HashCode Test");
        Text text2 = new Text("HashCode Test");

        assertEquals(text1.hashCode(), text2.hashCode(),
                "Хеш-коды одинаковых объектов должны совпадать");
    }

    /**
     * Проверка, что хеш-коды разных объектов, скорее всего, не совпадают.
     */
    @Test
    void hashCode_shouldBeDifferentForDifferentObjects() {
        Text text1 = new Text("ABC");
        Text text2 = new Text("DEF");

        assertNotEquals(text1.hashCode(), text2.hashCode(),
                "Хеш-коды разных объектов не должны совпадать (с высокой вероятностью)");
    }
}