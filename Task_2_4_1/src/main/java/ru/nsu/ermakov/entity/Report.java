package ru.nsu.ermakov.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Report {
    private final List<Group> groups;
    private final List<Task> tasks;
    private final List<Checkpoint> checkpoints;
    private final List<Assignment> assignments;
}
