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
/**
 * Построитель конфигурации курса, используемый DSL-скриптом.
 */
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

    /**
     * Пустой конструктор (используется в тестах).
     */
    public ConfigBuilder() {
        this.loader = null;
        this.baseDir = null;
        this.loadingStack = null;
    }

    /**
     * Внутренний конструктор, используемый при загрузке конфигурации с поддержкой include.
     */
    ConfigBuilder(ConfigLoader loader, Path baseDir, Set<Path> loadingStack) {
        this.loader = loader;
        this.baseDir = baseDir;
        this.loadingStack = loadingStack;
    }

    /**
     * Включает другой файл конфигурации относительно базовой директории.
     * @param filePath путь к файлу для include
     */
    public void include(String filePath) {
        if (loader == null || baseDir == null || loadingStack == null) {
            throw new ConfigIncludeException("include() is available only during config loading");
        }
        if (filePath == null || filePath.isBlank()) {
            throw new ConfigIncludeException("include path is empty");
        }

        Path includePath = baseDir.resolve(filePath).normalize().toAbsolutePath();
        try {
            Config imported = loader.loadConfig(includePath, loadingStack);
            merge(imported);
        } catch (IOException e) {
            throw new ConfigIncludeException("Failed to include config: " + includePath, e);
        }
    }

    /**
     * Обёртка для определения групп в DSL.
     */
    public void groups(Closure<?> closure) {
        closure.setDelegate(this);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();
    }

    /**
     * Обрабатывает блок определения одной группы.
     */
    public void group(Closure<?> closure) {
        GroupData groupData = new GroupData();
        closure.setDelegate(groupData);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();

        List<Student> studentList = new ArrayList<>();
        for (Map<String, String> studentMap : groupData.students) {
            String fio = studentMap.get("fio");
            String github = studentMap.get("githubNick");
            String repo = studentMap.get("repoUrl");
            studentList.add(new Student(fio, github, repo));
        }
        groups.add(new Group(groupData.name, studentList));
    }

    /**
     * Обёртка для определения задач в DSL.
     */
    public void tasks(Closure<?> closure) {
        closure.setDelegate(this);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();
    }

    /**
     * Обрабатывает блок определения одной задачи.
     */
    public void task(Closure<?> closure) {
        TaskData taskData = new TaskData();
        closure.setDelegate(taskData);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();

        String taskId = firstNonBlank(taskData.id, taskData.name, taskData.title);
        String taskName = firstNonBlank(taskData.title, taskData.name, taskData.id);
        LocalDateTime soft;
        if (taskData.softDeadline != null) {
            soft = LocalDate.parse(taskData.softDeadline, DATE_FORMATTER).atStartOfDay();
        } else {
            soft = null;
        }
        LocalDateTime hard;
        if (taskData.hardDeadline != null) {
            hard = LocalDate.parse(taskData.hardDeadline, DATE_FORMATTER).atStartOfDay();
        } else {
            hard = null;
        }

        tasks.add(new Task(taskId, taskName, soft, hard, taskData.maxScore));
    }

    /**
     * Обёртка для определения контрольных точек.
     */
    public void checkpoints(Closure<?> closure) {
        closure.setDelegate(this);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();
    }

    /**
     * Обрабатывает блок определения одной контрольной точки.
     */
    public void checkpoint(Closure<?> closure) {
        CheckpointData checkpointData = new CheckpointData();
        closure.setDelegate(checkpointData);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();

        LocalDateTime date;
        if (checkpointData.date != null) {
            date = LocalDate.parse(checkpointData.date, DATE_FORMATTER).atStartOfDay();
        } else {
            date = null;
        }
        checkpoints.add(new Checkpoint(checkpointData.name, date));
    }

    /**
     * Обёртка для определения индивидуальных назначений.
     */
    public void assignments(Closure<?> closure) {
        closure.setDelegate(this);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();
    }

    /**
     * Обрабатывает блок определения одного назначения (assignment).
     */
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

    /**
     * Обрабатывает блок настроек системы в DSL и сохраняет их.
     */
    public void settings(Closure<?> closure) {
        SettingsData data = new SettingsData(settings);
        closure.setDelegate(data);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();
        settings = data.toSystemSettings();
    }

    /**
     * Строит итоговый объект конфигурации.
     */
    public Config build() {
        return new Config(groups, tasks, checkpoints, taskSelections, settings);
    }

    /**
     * Сливает импортированную конфигурацию в текущую.
     */
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

    /**
     * Возвращает первый непустой из переданных значений или null.
     */
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

    /**
     * Вспомогательный класс для сбора данных группы в процессе парсинга DSL.
     */
    public static class GroupData {
        public String name;
        public List<Map<String, String>> students;
    }

    /**
     * Данные, собираемые для одной задачи из DSL.
     */
    public static class TaskData {
        public String id;
        public String title;
        public String name;
        public String softDeadline;
        public String hardDeadline;
        public byte maxScore;
    }

    /**
     * Данные контрольной точки из DSL.
     */
    public static class CheckpointData {
        public String name;
        public String date;
    }

    /**
     * Данные для индивидуального назначения задач студенту.
     */
    public static class AssignmentData {
        public String githubNick;
        public String fio;
        public List<String> tasks;
    }

    /**
     * Временный контейнер для настроек, используемый при разборе DSL.
     */
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
        public Integer semester;

        private final SystemSettings defaults;

        /**
         * Инициализирует контейнер значениями по умолчанию.
         */
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
            semester = this.defaults.getSemester();
        }

        /**
         * Преобразует собранные значения в объект SystemSettings.
         */
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
                    valueOrDefault(activityPenaltyThreshold, defaults.getActivityPenaltyThreshold()),
                    valueOrDefault(semester, defaults.getSemester())
            );
        }

        private long valueOrDefault(Long value, long fallback) {
            return value == null ? fallback : value;
        }

        private double valueOrDefault(Double value, double fallback) {
            return value == null ? fallback : value;
        }

        private int valueOrDefault(Integer value, int fallback) {
            return value == null ? fallback : value;
        }
    }
}
