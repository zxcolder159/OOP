package ru.nsu.ermakov.products;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Интерфейс для продуктов.
 * В лабе только 1 вид пиццы, но интерфейс добавит расширяемость кода, и вообще в стилистике ООП.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
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
    int getSize();

    /**
     * Геттер id.
     */
    int getId();

    /**
     * Геттер id.
     */
    int getOrderId();

    /**
     * Геттер времени готовки.
     */
    void setOrderId(int orderId);

    /**
     * Клонирование объекта.
     */
    Product clone();
}
