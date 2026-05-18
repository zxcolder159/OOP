package ru.nsu.ermakov.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

/**
 * Конфигурация курса, собранная из DSL: группы, задачи, контрольные точки и настройки.
 */
@Getter
@AllArgsConstructor
public class Config {
    private final List<Group> groups;
    private final List<Task> tasks;
    private final List<Checkpoint> checkpoints;
    private final List<StudentTaskSelection> taskSelections;
    private final SystemSettings settings;

    /**
     * Возвращает настройки системы или значения по умолчанию, если настройки не заданы.
     */
    public SystemSettings getSettings() {
        return settings == null ? SystemSettings.defaults() : settings;
    }

}
