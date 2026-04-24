package ru.nsu.ermakov.dsl;

import groovy.lang.Closure;
import lombok.Getter;
import ru.nsu.ermakov.entity.*;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class ConfigBuilder {
    private final List<Group> groups = new ArrayList<>();
    private final List<Task> tasks = new ArrayList<>();
    private final List<Checkpoint> checkpoints = new ArrayList<>();
    private final List<StudentTaskSelection> taskSelections = new ArrayList<>();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private SystemSettings settings = SystemSettings.defaults();
    private final ConfigLoader loader;
    private final Path baseDir;
    private final Set<Path> loadingStack;

    public ConfigBuilder() {
        this.loader = null;
        this.baseDir = null;
        this.loadingStack = null;
    }

    ConfigBuilder(ConfigLoader loader, Path baseDir, Set<Path> loadingStack) {
        this.loader = loader;
        this.baseDir = baseDir;
        this.loadingStack = loadingStack;
    }

    public void include(String filePath) {
        if (loader == null || baseDir == null || loadingStack == null) {
            throw new IllegalStateException("include() is available only during config loading");
        }
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("include path is empty");
        }

        Path includePath = baseDir.resolve(filePath).normalize().toAbsolutePath();
        try {
            Config imported = loader.loadConfig(includePath, loadingStack);
            merge(imported);
        } catch (IOException e) {
            throw new RuntimeException("Failed to include config: " + includePath, e);
        }
    }

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

        String taskId = firstNonBlank(taskData.id, taskData.name, taskData.title);
        String taskName = firstNonBlank(taskData.title, taskData.name, taskData.id);
        LocalDateTime soft = taskData.softDeadline != null ? LocalDate.parse(taskData.softDeadline, DATE_FORMATTER).atStartOfDay() : null;
        LocalDateTime hard = taskData.hardDeadline != null ? LocalDate.parse(taskData.hardDeadline, DATE_FORMATTER).atStartOfDay() : null;

        tasks.add(new Task(taskId, taskName, soft, hard, taskData.maxScore));
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

    public void assignments(Closure<?> closure) {
        closure.setDelegate(this);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();
    }

    public void assignment(Closure<?> closure) {
        AssignmentData assignmentData = new AssignmentData();
        closure.setDelegate(assignmentData);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();

        taskSelections.add(new StudentTaskSelection(
                assignmentData.githubNick,
                assignmentData.fio,
                assignmentData.tasks
        ));
    }

    public void settings(Closure<?> closure) {
        SettingsData data = new SettingsData(settings);
        closure.setDelegate(data);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();
        settings = data.toSystemSettings();
    }

    public Config build() {
        return new Config(groups, tasks, checkpoints, taskSelections, settings);
    }

    private void merge(Config imported) {
        if (imported == null) {
            return;
        }

        if (imported.getGroups() != null) {
            groups.addAll(imported.getGroups());
        }
        if (imported.getTasks() != null) {
            tasks.addAll(imported.getTasks());
        }
        if (imported.getCheckpoints() != null) {
            checkpoints.addAll(imported.getCheckpoints());
        }
        if (imported.getTaskSelections() != null) {
            taskSelections.addAll(imported.getTaskSelections());
        }
        if (imported.getSettings() != null) {
            settings = imported.getSettings();
        }
    }

    private String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    public static class GroupData {
        public String name;
        public List<Map<String, String>> students;
    }

    public static class TaskData {
        public String id;
        public String title;
        public String name;
        public String softDeadline;
        public String hardDeadline;
        public byte maxScore;
    }

    public static class CheckpointData {
        public String name;
        public String date;
    }

    public static class AssignmentData {
        public String githubNick;
        public String fio;
        public List<String> tasks;
    }

    public static class SettingsData {
        public Long buildTimeoutSeconds;
        public Long gitTimeoutSeconds;
        public Double compilePart;
        public Double docsStylePart;
        public Double testsPart;
        public Double deadlineMissPenalty;
        public Double maxDeadlinePenalty;
        public Double excellentThreshold;
        public Double goodThreshold;
        public Double satisfactoryThreshold;
        public Double activityBonusThreshold;
        public Double activityPenaltyThreshold;

        private final SystemSettings defaults;

        SettingsData(SystemSettings defaults) {
            this.defaults = defaults == null ? SystemSettings.defaults() : defaults;

            buildTimeoutSeconds = this.defaults.getBuildTimeoutSeconds();
            gitTimeoutSeconds = this.defaults.getGitTimeoutSeconds();
            compilePart = this.defaults.getCompilePart();
            docsStylePart = this.defaults.getDocsStylePart();
            testsPart = this.defaults.getTestsPart();
            deadlineMissPenalty = this.defaults.getDeadlineMissPenalty();
            maxDeadlinePenalty = this.defaults.getMaxDeadlinePenalty();
            excellentThreshold = this.defaults.getExcellentThreshold();
            goodThreshold = this.defaults.getGoodThreshold();
            satisfactoryThreshold = this.defaults.getSatisfactoryThreshold();
            activityBonusThreshold = this.defaults.getActivityBonusThreshold();
            activityPenaltyThreshold = this.defaults.getActivityPenaltyThreshold();
        }

        SystemSettings toSystemSettings() {
            return new SystemSettings(
                    valueOrDefault(buildTimeoutSeconds, defaults.getBuildTimeoutSeconds()),
                    valueOrDefault(gitTimeoutSeconds, defaults.getGitTimeoutSeconds()),
                    valueOrDefault(compilePart, defaults.getCompilePart()),
                    valueOrDefault(docsStylePart, defaults.getDocsStylePart()),
                    valueOrDefault(testsPart, defaults.getTestsPart()),
                    valueOrDefault(deadlineMissPenalty, defaults.getDeadlineMissPenalty()),
                    valueOrDefault(maxDeadlinePenalty, defaults.getMaxDeadlinePenalty()),
                    valueOrDefault(excellentThreshold, defaults.getExcellentThreshold()),
                    valueOrDefault(goodThreshold, defaults.getGoodThreshold()),
                    valueOrDefault(satisfactoryThreshold, defaults.getSatisfactoryThreshold()),
                    valueOrDefault(activityBonusThreshold, defaults.getActivityBonusThreshold()),
                    valueOrDefault(activityPenaltyThreshold, defaults.getActivityPenaltyThreshold())
            );
        }

        private long valueOrDefault(Long value, long fallback) {
            return value == null ? fallback : value;
        }

        private double valueOrDefault(Double value, double fallback) {
            return value == null ? fallback : value;
        }
    }
}
