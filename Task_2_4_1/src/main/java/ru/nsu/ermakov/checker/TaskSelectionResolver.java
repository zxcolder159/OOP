package ru.nsu.ermakov.checker;

import ru.nsu.ermakov.entity.Config;
import ru.nsu.ermakov.entity.Student;
import ru.nsu.ermakov.entity.StudentTaskSelection;
import ru.nsu.ermakov.entity.Task;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class TaskSelectionResolver {

    public boolean shouldCheckStudent(Config config, Student student) {
        List<StudentTaskSelection> matches = findMatches(config, student);
        if (!hasConfiguredSelections(config)) {
            return true;
        }
        return !matches.isEmpty();
    }

    public List<Task> resolveTasks(Config config, Student student, List<Task> allTasks) {
        if (allTasks == null || allTasks.isEmpty()) {
            return List.of();
        }

        List<StudentTaskSelection> matches = findMatches(config, student);
        if (!hasConfiguredSelections(config)) {
            return allTasks;
        }
        if (matches.isEmpty()) {
            return List.of();
        }

        Set<String> selectedNames = new LinkedHashSet<>();
        for (StudentTaskSelection match : matches) {
            List<String> names = match.getTaskNames();
            if (names == null || names.isEmpty()) {
                return allTasks;
            }
            for (String name : names) {
                String normalized = normalize(name);
                if (!normalized.isEmpty()) {
                    selectedNames.add(normalized);
                }
            }
        }

        if (selectedNames.isEmpty()) {
            return allTasks;
        }

        List<Task> filtered = new ArrayList<>();
        for (Task task : allTasks) {
            if (task == null) {
                continue;
            }
            String taskName = normalize(task.getName());
            String taskId = normalize(task.getId());
            if (selectedNames.contains(taskName) || selectedNames.contains(taskId)) {
                filtered.add(task);
            }
        }
        return filtered;
    }

    private boolean hasConfiguredSelections(Config config) {
        return config != null && config.getTaskSelections() != null && !config.getTaskSelections().isEmpty();
    }

    private List<StudentTaskSelection> findMatches(Config config, Student student) {
        if (config == null || config.getTaskSelections() == null || student == null) {
            return List.of();
        }

        String studentNick = normalize(student.getGithubNick());
        String studentFio = normalize(student.getFio());

        List<StudentTaskSelection> matches = new ArrayList<>();
        for (StudentTaskSelection selection : config.getTaskSelections()) {
            if (selection == null) {
                continue;
            }
            String selectionNick = normalize(selection.getGithubNick());
            String selectionFio = normalize(selection.getFio());
            boolean nickMatches = !selectionNick.isEmpty() && selectionNick.equals(studentNick);
            boolean fioMatches = !selectionFio.isEmpty() && selectionFio.equals(studentFio);
            if (nickMatches || fioMatches) {
                matches.add(selection);
            }
        }
        return matches;
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }
}

