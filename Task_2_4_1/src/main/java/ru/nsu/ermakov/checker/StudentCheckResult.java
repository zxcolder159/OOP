package ru.nsu.ermakov.checker;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.ermakov.entity.Student;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Итоговый результат проверки студента: баллы, задачи, и метрики активности.
 */
@Getter
@Setter
public class StudentCheckResult {
    private String groupName;
    private Student student;
    private String repoPath;
    private String cloneError;
    private final List<TaskCheckResult> taskResults = new ArrayList<>();
    private int totalScore;
    private int maxScore;
    private final Map<String, Integer> checkpointScores = new LinkedHashMap<>();
    private final Map<String, Integer> checkpointGrades = new LinkedHashMap<>();
    private int finalGrade;
    private int activeWeeks;
    private int totalWeeks;
    private double activityRatio;

    /**
     * Создаёт контейнер результата для конкретного студента и группы.
     */
    public StudentCheckResult(String groupName, Student student) {
        this.groupName = groupName;
        this.student = student;
    }

    /**
     * Устанавливает локальный путь к репозиторию и очищает ошибку клонирования.
     */
    public void markRepoPath(String repoPath) {
        this.repoPath = repoPath;
        this.cloneError = null;
    }

    /**
     * Указывает ошибку при клонировании репозитория.
     */
    public void markCloneError(String cloneError) {
        this.cloneError = cloneError;
    }

    /**
     * Признак наличия ошибки при клонировании.
     */
    public boolean hasCloneError() {
        return cloneError != null && !cloneError.isBlank();
    }

    /**
     * Добавляет результат проверки одной задачи.
     */
    public void addTaskResult(TaskCheckResult taskResult) {
        if (taskResult != null) {
            this.taskResults.add(taskResult);
        }
    }

    /**
     * Применяет статистику активности к результату студента.
     */
    public void applyActivity(ActivityStats stats) {
        if (stats == null) {
            this.activeWeeks = 0;
            this.totalWeeks = 0;
            this.activityRatio = 0.0;
            return;
        }

        this.activeWeeks = stats.activeWeeks();
        this.totalWeeks = stats.totalWeeks();
        this.activityRatio = stats.ratio();
    }
}
