package ru.nsu.ermakov.checker;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.ermakov.entity.Status;
import ru.nsu.ermakov.entity.Task;
import java.time.LocalDateTime;

/**
 * Результат проверки одной задачи для студента.
 */
@Getter
@Setter
public class TaskCheckResult {
    private Task task;
    private Status status = Status.NOT_SUBMITTED;
    private int score;
    private boolean compilePassed;
    private boolean docsStylePassed;
    private boolean testsExecuted;
    private int testsPassed;
    private int testsFailed;
    private int testsSkipped;
    private String note = "";
    private LocalDateTime lastCommitDate;
    private String taskPath;

    /**
     * Создаёт результат для заданной задачи.
     */
    public TaskCheckResult(Task task) {
        this.task = task;
    }

    /**
     * Отмечает задачу как не представлена.
     */
    public void markNotSubmitted(String note) {
        this.status = Status.NOT_SUBMITTED;
        this.note = note == null ? "" : note;
        this.score = 0;
    }

    /**
     * Отмечает задачу как представлена и сохраняет путь и дату коммита.
     */
    public void markSubmitted(String taskPath, LocalDateTime commitDate) {
        this.status = Status.SUBMITTED;
        this.taskPath = taskPath;
        this.lastCommitDate = commitDate;
    }

    /**
     * Отмечает задачу как пройденную.
     */
    public void markPassed(String note) {
        this.status = Status.PASSED;
        this.note = note == null ? "" : note;
    }

    /**
     * Отмечает задачу как проваленную.
     */
    public void markFailed(String note) {
        this.status = Status.FAILED;
        this.note = note == null ? "" : note;
    }

    /**
     * Возвращает общее число тестов для задачи.
     */
    public int totalTests() {
        return testsPassed + testsFailed + testsSkipped;
    }
}
