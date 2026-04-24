package ru.nsu.ermakov.entity;

import lombok.Getter;

import java.util.List;

@Getter
public class StudentTaskSelection {
    private final String githubNick;
    private final String fio;
    private final List<String> taskNames;

    public StudentTaskSelection(String githubNick, String fio, List<String> taskNames) {
        this.githubNick = githubNick;
        this.fio = fio;
        this.taskNames = taskNames == null ? List.of() : List.copyOf(taskNames);
    }
}

