package ru.nsu.ermakov.products;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Интерфейс для продуктов.
 * В лабе есть только 1 вид пиццы, но этот интерфейс добавит расширяемость кода, и вообще в стилистике ООП.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type" // Поле в JSON, по которому определим класс
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Pizza.class, name = "pizza"),
        @JsonSubTypes.Type(value = Burger.class, name = "burger"),
        @JsonSubTypes.Type(value = CocaCola.class, name = "cola")
})
public interface Product {
    /**
     * Геттер размера.
     */
    public int getSize();
    /**
     * Геттер id.
     */
    public int getId();
    /**
     * Геттер времени готовки.
     */
    public long getCookingTime();

    /**
     * Геттер id.
     */
    public int getOrderId();
    /**
     * Геттер времени готовки.
     */
    public void setOrderId(int orderId);
    /**
     * Клонирование объекта.
     */
    public Product clone();
}
