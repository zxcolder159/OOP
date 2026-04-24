package ru.nsu.ermakov.checker;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.ermakov.entity.Status;
import ru.nsu.ermakov.entity.Task;

import java.time.LocalDateTime;

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

    public TaskCheckResult(Task task) {
        this.task = task;
    }

    public void markNotSubmitted(String note) {
        this.status = Status.NOT_SUBMITTED;
        this.note = note == null ? "" : note;
        this.score = 0;
    }

    public void markSubmitted(String taskPath, LocalDateTime commitDate) {
        this.status = Status.SUBMITTED;
        this.taskPath = taskPath;
        this.lastCommitDate = commitDate;
    }

    public void markPassed(String note) {
        this.status = Status.PASSED;
        this.note = note == null ? "" : note;
    }

    public void markFailed(String note) {
        this.status = Status.FAILED;
        this.note = note == null ? "" : note;
    }

    public int totalTests() {
        return testsPassed + testsFailed + testsSkipped;
    }
}
