package ru.nsu.ermakov.products;

public class Pizza implements Product {
    private final int id;

    /**
     * Конструктор как трек у Бабангиды.
     */
    public Pizza (int id) {
        this.id = id;
    }

    /**
     * Геттер id.
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Геттер размера.
     */
    @Override
    public int getSize () {
        return 1;
    }
}
