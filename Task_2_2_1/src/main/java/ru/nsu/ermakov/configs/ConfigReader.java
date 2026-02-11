package ru.nsu.ermakov.configs;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ConfigReader {
    private final ObjectMapper mapper = new ObjectMapper();

    public PizzaConfig readPizzaConfig(String path) throws IOException {
        return mapper.readValue(new File(path), PizzaConfig.class);
    }

    public OrderConfig readOrderConfig(String path) throws IOException {
        return mapper.readValue(new File(path), OrderConfig.class);
    }

    public WarehouseConfig readWarehouseConfig(String path) throws IOException {
        return mapper.readValue(new File(path), WarehouseConfig.class);
    }
}
