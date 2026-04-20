package ru.nsu.ermakov.dsl;

import groovy.lang.Closure;
import lombok.Getter;
import ru.nsu.ermakov.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class ConfigBuilder {
    private final List<Group> groups = new ArrayList<>();
    private final List<Task> tasks = new ArrayList<>();
    private final List<Checkpoint> checkpoints = new ArrayList<>();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public void groups(Closure<?> closure) {
        closure.setDelegate(this);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();
    }

    public void group(Closure<?> closure) {
        GroupData groupData = new GroupData();
        closure.setDelegate(groupData);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();

        List<Student> studentList = new ArrayList<>();
        for (Map<String, String> studentMap : groupData.students) {
            studentList.add(new Student(studentMap.get("fio"), studentMap.get("githubNick"), studentMap.get("repoUrl")));
        }
        groups.add(new Group(groupData.name, studentList));
    }

    public void tasks(Closure<?> closure) {
        closure.setDelegate(this);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();
    }

    public void task(Closure<?> closure) {
        TaskData taskData = new TaskData();
        closure.setDelegate(taskData);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();

        LocalDateTime soft = taskData.softDeadline != null ? LocalDate.parse(taskData.softDeadline, DATE_FORMATTER).atStartOfDay() : null;
        LocalDateTime hard = taskData.hardDeadline != null ? LocalDate.parse(taskData.hardDeadline, DATE_FORMATTER).atStartOfDay() : null;

        tasks.add(new Task(taskData.name, soft, hard, taskData.maxScore));
    }

    public void checkpoints(Closure<?> closure) {
        closure.setDelegate(this);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();
    }

    public void checkpoint(Closure<?> closure) {
        CheckpointData checkpointData = new CheckpointData();
        closure.setDelegate(checkpointData);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();

        LocalDateTime date = checkpointData.date != null ? LocalDate.parse(checkpointData.date, DATE_FORMATTER).atStartOfDay() : null;
        checkpoints.add(new Checkpoint(checkpointData.name, date));
    }

    public Config build() {
        return new Config(groups, tasks, checkpoints);
    }

    public static class GroupData {
        public String name;
        public List<Map<String, String>> students;
    }

    public static class TaskData {
        public String name;
        public String softDeadline;
        public String hardDeadline;
        public byte maxScore;
    }

    public static class CheckpointData {
        public String name;
        public String date;
    }
}
