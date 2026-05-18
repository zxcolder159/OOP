package ru.nsu.ermakov.entity;

import lombok.Getter;
import java.util.List;

/**
 * Описание выбора задач для конкретного студента (по нику или ФИО).
 */
@Getter
public class StudentTaskSelection {
    private final String githubNick;
    private final String fio;
    private final List<String> taskNames;

    /**
     * @param githubNick GitHub ник студента
     * @param fio ФИО студента
     * @param taskNames список имён задач
     */
    public StudentTaskSelection(String githubNick, String fio, List<String> taskNames) {
        this.githubNick = githubNick;
        this.fio = fio;
        this.taskNames = taskNames == null ? List.of() : List.copyOf(taskNames);
    }
}

